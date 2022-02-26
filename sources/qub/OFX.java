package qub;

/**
 * A general purpose type that contains functions that can be used to interact with OFX data.
 */
public interface OFX
{
    /**
     * Parse an {@link OFXDocument} from the provided text.
     * @param text The text to parse.
     * @return The parsed {@link OFXDocument}.
     */
    static Result<OFXDocument> parse(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return OFX.parse(OFXTokenizer.create(text));
    }

    /**
     * Parse an {@link OFXDocument} from the provided {@link CharacterReadStream}.
     * @param stream The {@link CharacterReadStream} to parse.
     * @return The parsed {@link OFXDocument}.
     */
    static Result<OFXDocument> parse(CharacterReadStream stream)
    {
        PreCondition.assertNotNull(stream, "stream");

        return OFX.parse(OFXTokenizer.create(stream));
    }

    /**
     * Parse an {@link OFXDocument} from the provided characters.
     * @param characters The characters to parse.
     * @return The parsed {@link OFXDocument}.
     */
    static Result<OFXDocument> parse(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return OFX.parse(OFXTokenizer.create(characters));
    }

    /**
     * Parse an {@link OFXDocument} from the provided {@link OFXTokenizer}.
     * @param tokenizer The {@link OFXTokenizer} to get {@link OFXToken}s from.
     * @return The parsed {@link OFXDocument}.
     */
    static Result<OFXDocument> parse(OFXTokenizer tokenizer)
    {
        PreCondition.assertNotNull(tokenizer, "tokenizer");

        return Result.create(() ->
        {
              tokenizer.start();

              final CharacterList headerText = CharacterList.create();
              OFXAggregateChild root = null;
              while (tokenizer.hasCurrent())
              {
                  switch (tokenizer.getCurrent().getType())
                  {
                      case Text:
                          if (root != null)
                          {
                              throw new ParseException("Header text is allowed only before the root.");
                          }
                          else
                          {
                              final OFXText text = tokenizer.getCurrentText();
                              headerText.addAll(text.getText());
                              tokenizer.next();
                          }
                          break;

                      case Whitespace:
                          if (root == null)
                          {
                              final OFXText whitespace = tokenizer.getCurrentText();
                              headerText.addAll(whitespace.getText());
                              tokenizer.next();
                          }
                          break;

                      case StartTag:
                          if (root != null)
                          {
                              throw new ParseException("Only one root is allowed in an OFX document.");
                          }
                          else
                          {
                              root = OFX.parseAggregateChild(tokenizer);
                          }
                          break;

                      case EndTag:
                          throw new ParseException("Expected text or root start tag, but found " + tokenizer.getCurrent().toString() + " instead.");
                  }
              }

              if (root == null)
              {
                  throw new ParseException("Missing root.");
              }

              return OFXDocument.create(headerText.toString(), root);
        });
    }

    static OFXAggregateChild parseAggregateChild(OFXTokenizer tokenizer)
    {
        PreCondition.assertNotNull(tokenizer, "tokenizer");
        PreCondition.assertTrue(tokenizer.hasStarted(), "tokenizer.hasStarted()");
        PreCondition.assertTrue(tokenizer.hasCurrent(), "tokenizer.hasCurrent()");
        PreCondition.assertEqual(OFXTokenType.StartTag, tokenizer.getCurrent().getType(), "tokenizer.getCurrent().getType()");

        final OFXTag startTag = tokenizer.getCurrentTag();
        if (!tokenizer.next())
        {
            throw new ParseException("Missing aggregate end tag or element data for " + startTag.toString() + ".");
        }

        final List<OFXText> elementData = List.create();
        final List<OFXAggregateChild> children = List.create();
        OFXAggregateChild result = null;
        while (result == null && tokenizer.hasCurrent())
        {
            switch (tokenizer.getCurrent().getType())
            {
                case Whitespace:
                    if (!children.any())
                    {
                        elementData.add(tokenizer.getCurrentText());
                    }
                    tokenizer.next();
                    break;

                case Text:
                    if (children.any())
                    {
                        throw new ParseException("An OFX aggregate node is not allowed to have text data.");
                    }
                    elementData.add(tokenizer.getCurrentText());
                    tokenizer.next();
                    break;

                case StartTag:
                    if (elementData.contains((OFXText text) -> text.getType() == OFXTokenType.Text))
                    {
                        result = OFXElement.create(startTag.getName(), Strings.join(elementData.map(OFXText::getText)));
                    }
                    else
                    {
                        children.add(OFX.parseAggregateChild(tokenizer));
                    }
                    elementData.clear();
                    break;

                case EndTag:
                    if (elementData.contains((OFXText text) -> text.getType() == OFXTokenType.Text))
                    {
                        result = OFXElement.create(startTag.getName(), Strings.join(elementData.map(OFXText::getText)));
                    }
                    else
                    {
                        final OFXTag endTag = tokenizer.getCurrentTag();
                        if (!startTag.getName().equals(endTag.getName()))
                        {
                            throw new ParseException("Expected end tag for " + startTag.toString() + ", but found " + endTag.toString() + " instead.");
                        }
                        else if (!children.any())
                        {
                            throw new ParseException("Missing aggregate children.");
                        }
                        else
                        {
                            result = OFXAggregate.create(startTag.getName(), children);
                            tokenizer.next();
                        }
                    }
                    break;
            }
        }

        if (result == null)
        {
            if (children.any())
            {
                throw new ParseException("Missing end tag for " + startTag.toString() + ".");
            }
            else if (!elementData.contains((OFXText text) -> text.getType() == OFXTokenType.Text))
            {
                throw new ParseException("Missing element data or aggregate children for " + startTag.toString() + ".");
            }

            result = OFXElement.create(startTag.getName(), Strings.join(elementData.map(OFXText::getText)));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
