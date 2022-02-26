package qub;

/**
 * An element within an {@link OFXDocument} that contains text data.
 */
public class OFXElement extends OFXAggregateChildBase<OFXElement>
{
    private final String name;
    private final String data;

    private OFXElement(String name, String data)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNullAndNotEmpty(data, "data");

        this.name = name;
        this.data = data;
    }

    public static OFXElement create(String name, String data)
    {
        return new OFXElement(name, data);
    }

    public String getName()
    {
        return this.name;
    }

    public String getData()
    {
        return this.data;
    }

    public String getTrimmedData()
    {
        return this.getData().trim();
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
            result += writeStream.write(this.getName()).await();
            result += writeStream.write('>').await();
            result += writeStream.write(this.getData()).await();

            PostCondition.assertGreaterThanOrEqualTo(result, 4, "result");

            return result;
        });
    }

    @Override
    public boolean equals(OFXElement rhs)
    {
        return rhs != null &&
            this.getName().equals(rhs.getName()) &&
            this.getTrimmedData().equals(rhs.getTrimmedData());
    }
}
