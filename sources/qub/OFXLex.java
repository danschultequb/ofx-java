package qub;

/**
 * A single lex within an OFX document.
 */
public class OFXLex
{
    private final OFXLexType type;
    private final String text;

    private OFXLex(OFXLexType type, String text)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.type = type;
        this.text = text;
    }

    private static final OFXLex leftAngleBracket = new OFXLex(OFXLexType.LeftAngleBracket, "<");
    public static OFXLex leftAngleBracket()
    {
        return OFXLex.leftAngleBracket;
    }

    private static final OFXLex rightAngleBracket = new OFXLex(OFXLexType.RightAngleBracket, ">");
    public static OFXLex rightAngleBracket()
    {
        return OFXLex.rightAngleBracket;
    }

    private static final OFXLex forwardSlash = new OFXLex(OFXLexType.ForwardSlash, "/");
    public static OFXLex forwardSlash()
    {
        return OFXLex.forwardSlash;
    }

    public static OFXLex whitespace(String text)
    {
        return new OFXLex(OFXLexType.Whitespace, text);
    }

    public static OFXLex text(String text)
    {
        return new OFXLex(OFXLexType.Text, text);
    }

    public OFXLexType getType()
    {
        return this.type;
    }

    public String getText()
    {
        return this.text;
    }

    @Override
    public String toString()
    {
        return "Type: " + this.type + ", Text: " + Strings.escapeAndQuote(this.text);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof OFXLex && this.equals((OFXLex)rhs);
    }

    public boolean equals(OFXLex rhs)
    {
        return rhs != null &&
            this.type == rhs.type &&
            this.text.equals(rhs.text);
    }
}
