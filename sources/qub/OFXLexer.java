package qub;

/**
 * An object that lexes a sequence of characters into a sequences of {@link OFXLex}s.
 */
public class OFXLexer implements Iterator<OFXLex>
{
    private final Iterator<Character> characters;

    private boolean hasStarted;
    private OFXLex current;

    private OFXLexer(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        this.characters = characters;
    }

    public static OFXLexer create(Iterator<Character> characters)
    {
        return new OFXLexer(characters);
    }

    public static OFXLexer create(CharacterReadStream readStream)
    {
        PreCondition.assertNotNull(readStream, "readStream");

        return OFXLexer.create(CharacterReadStream.iterate(readStream));
    }

    public static OFXLexer create(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return OFXLexer.create(Strings.iterate(text));
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.current != null;
    }

    @Override
    public OFXLex getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.current;
    }

    @Override
    public boolean next()
    {
        if (!this.hasStarted())
        {
            this.hasStarted = true;
            this.characters.start();
        }

        if (!this.characters.hasCurrent())
        {
            this.current = null;
        }
        else
        {
            final char currentCharacter = this.characters.getCurrent();
            switch (currentCharacter)
            {
                case '<':
                    this.current = OFXLex.leftAngleBracket();
                    this.characters.next();
                    break;

                case '>':
                    this.current = OFXLex.rightAngleBracket();
                    this.characters.next();
                    break;

                case '/':
                    this.current = OFXLex.forwardSlash();
                    this.characters.next();
                    break;

                default:
                    if (Characters.isWhitespace(currentCharacter))
                    {
                        final String whitespace = Characters.join(this.characters.takeWhile(Characters::isWhitespace));
                        this.current = OFXLex.whitespace(whitespace);
                    }
                    else
                    {
                        final String text = Characters.join(this.characters.takeUntil((Character c) ->
                        {
                            return c == '<' || c == '>' || c == '/' || Characters.isWhitespace(c);
                        }));
                        this.current = OFXLex.text(text);
                    }
                    break;
            }
        }

        return this.hasCurrent();
    }
}
