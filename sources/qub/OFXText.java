package qub;

public class OFXText extends OFXTokenBase<OFXText>
{
    private final OFXTokenType type;
    private final String text;

    private OFXText(OFXTokenType type, String text)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.type = type;
        this.text = text;
    }

    public static OFXText whitespace(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return new OFXText(OFXTokenType.Whitespace, text);
    }

    public static OFXText text(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return new OFXText(OFXTokenType.Text, text);
    }

    @Override
    public OFXTokenType getType()
    {
        return this.type;
    }

    public String getText()
    {
        return this.text;
    }

    @Override
    public boolean equals(OFXText rhs)
    {
        return rhs != null &&
            this.type == rhs.type &&
            this.text.equals(rhs.text);
    }

    @Override
    public Result<Integer> writeTo(CharacterWriteStream writeStream, OFXFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotNull(format, "format");

        return writeStream.write(this.getText());
    }
}
