package qub;

public class OFXTokenizer implements Iterator<OFXToken>
{
    private final OFXLexer lexer;

    private boolean hasStarted;
    private OFXToken current;

    private OFXTokenizer(OFXLexer lexer)
    {
        PreCondition.assertNotNull(lexer, "lexer");

        this.lexer = lexer;
    }

    public static OFXTokenizer create(OFXLexer lexer)
    {
        return new OFXTokenizer(lexer);
    }

    public static OFXTokenizer create(CharacterReadStream readStream)
    {
        PreCondition.assertNotNull(readStream, "readStream");

        return OFXTokenizer.create(OFXLexer.create(readStream));
    }

    public static OFXTokenizer create(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return OFXTokenizer.create(OFXLexer.create(characters));
    }

    public static OFXTokenizer create(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return OFXTokenizer.create(OFXLexer.create(text));
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
    public OFXToken getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.current;
    }

    public OFXText getCurrentText()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");
        PreCondition.assertInstanceOf(this.getCurrent(), OFXText.class, "this.getCurrent()");

        return (OFXText)this.getCurrent();
    }

    public OFXTag getCurrentTag()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");
        PreCondition.assertInstanceOf(this.getCurrent(), OFXTag.class, "this.getCurrent()");

        return (OFXTag)this.getCurrent();
    }

    @Override
    public boolean next()
    {
        if (!this.hasStarted())
        {
            this.hasStarted = true;
            this.lexer.start();
        }

        if (!this.lexer.hasCurrent())
        {
            this.current = null;
        }
        else
        {
            switch (this.lexer.getCurrent().getType())
            {
                case LeftAngleBracket:
                    if (!this.lexer.next())
                    {
                        throw new ParseException("Missing start tag name or end tag forward slash ('/').");
                    }
                    else
                    {
                        String tagName;
                        boolean isStartTag;
                        switch (this.lexer.getCurrent().getType())
                        {
                            case Text:
                                tagName = this.lexer.getCurrent().getText();
                                isStartTag = true;
                                break;

                            case ForwardSlash:
                                if (!this.lexer.next())
                                {
                                    throw new ParseException("Missing end tag name.");
                                }
                                else if (this.lexer.getCurrent().getType() != OFXLexType.Text)
                                {
                                    throw new ParseException("Expected end tag name, but found " + Strings.escapeAndQuote(this.lexer.getCurrent().getText()) + " instead.");
                                }
                                else
                                {
                                    tagName = this.lexer.getCurrent().getText();
                                    isStartTag = false;
                                }
                                break;

                            default:
                                throw new ParseException("Expected start tag name or end tag forward slash ('/'), but found " + Strings.escapeAndQuote(this.lexer.getCurrent().getText()) + " instead.");
                        }

                        if (!this.lexer.next())
                        {
                            throw new ParseException("Missing " + (isStartTag ? "start" : "end") + " tag right angle bracket ('>').");
                        }
                        else if (this.lexer.getCurrent().getType() != OFXLexType.RightAngleBracket)
                        {
                            throw new ParseException("Expected " + (isStartTag ? "start" : "end") + " tag right angle bracket ('>'), but found " + Strings.escapeAndQuote(this.lexer.getCurrent().getText()) + " instead.");
                        }
                        else
                        {
                            this.current = isStartTag
                                ? OFXTag.startTag(tagName)
                                : OFXTag.endTag(tagName);
                            this.lexer.next();
                        }
                    }
                    break;

                case Whitespace:
                    this.current = OFXText.whitespace(this.lexer.getCurrent().getText());
                    this.lexer.next();
                    break;

                default:
                    this.current = OFXText.text(this.lexer.getCurrent().getText());
                    this.lexer.next();
                    break;
            }
        }

        return this.hasCurrent();
    }
}
