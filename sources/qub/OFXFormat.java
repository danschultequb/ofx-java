package qub;

/**
 * An object that can be used to format an {@link OFXObject}.
 */
public class OFXFormat
{
    public static final OFXFormat consise = OFXFormat.create();

    public static final OFXFormat pretty = OFXFormat.create()
        .setNewLine("\n")
        .setSingleIndent("  ");

    private String newLine;
    private String singleIndent;

    private OFXFormat()
    {
        this.newLine = "";
        this.singleIndent = "";
    }

    /**
     * Create a new {@link OFXFormat} object.
     * @return A new {@link OFXFormat} object.
     */
    public static OFXFormat create()
    {
        return new OFXFormat();
    }

    public OFXFormat setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }

    public String getNewLine()
    {
        return this.newLine;
    }

    public OFXFormat setSingleIndent(String singleIndent)
    {
        PreCondition.assertNotNull(singleIndent, "singleIndent");

        this.singleIndent = singleIndent;

        return this;
    }

    public String getSingleIndent()
    {
        return this.singleIndent;
    }
}
