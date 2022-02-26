package qub;

public class OFXTag extends OFXTokenBase<OFXTag>
{
    private final OFXTokenType type;
    private final String name;

    private OFXTag(OFXTokenType type, String name)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.type = type;
        this.name = name;
    }

    public static OFXTag startTag(String name)
    {
        return new OFXTag(OFXTokenType.StartTag, name);
    }

    public static OFXTag endTag(String name)
    {
        return new OFXTag(OFXTokenType.EndTag, name);
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public OFXTokenType getType()
    {
        return this.type;
    }

    @Override
    public boolean equals(OFXTag rhs)
    {
        return rhs != null &&
            this.getType() == rhs.getType() &&
            this.getName().equals(rhs.getName());
    }

    @Override
    public Result<Integer> writeTo(CharacterWriteStream writeStream, OFXFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotNull(format, "format");

        return Result.create(() ->
        {
            int result = 0;

            result += writeStream.write('<').await();
            if (this.getType() == OFXTokenType.EndTag)
            {
                result += writeStream.write('/').await();
            }
            result += writeStream.write(this.getName()).await();
            result += writeStream.write('>').await();

            return result;
        });
    }
}
