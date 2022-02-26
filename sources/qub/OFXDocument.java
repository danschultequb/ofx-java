package qub;

/**
 * A complete OFX document.
 */
public class OFXDocument extends OFXObjectBase<OFXDocument>
{
    private final String headerText;
    private final OFXAggregateChild root;

    private OFXDocument(String headerText, OFXAggregateChild root)
    {
        PreCondition.assertNotNull(headerText, "headerText");
        PreCondition.assertNotNull(root, "root");

        this.headerText = headerText;
        this.root = root;
    }

    public static OFXDocument create(String headerText, OFXAggregateChild root)
    {
        return new OFXDocument(headerText, root);
    }

    @Override
    public Result<Integer> writeTo(CharacterWriteStream writeStream, OFXFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotNull(format, "format");

        return Result.create(() ->
        {
            int result = 0;

            result += writeStream.write(headerText).await();
            result += this.root.writeTo(writeStream, format).await();

            return result;
        });
    }

    @Override
    public boolean equals(OFXDocument rhs)
    {
        return rhs != null &&
            this.headerText.equals(rhs.headerText) &&
            this.root.equals(rhs.root);
    }
}
