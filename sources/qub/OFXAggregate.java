package qub;

/**
 * An aggregate of {@link OFXElement} or child {@link OFXAggregate} objects within an
 * {@link OFXDocument}.
 */
public class OFXAggregate extends OFXAggregateChildBase<OFXAggregate>
{
    private final String name;
    private final Iterable<OFXAggregateChild> children;

    private OFXAggregate(String name, Iterable<OFXAggregateChild> children)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNullAndNotEmpty(children, "children");

        this.name = name;
        this.children = children;
    }

    public static OFXAggregate create(String name, Iterable<OFXAggregateChild> children)
    {
        return new OFXAggregate(name, children);
    }

    public String getName()
    {
        return this.name;
    }

    /**
     * Get the children objects of this {@link OFXAggregate}.
     * @return The children objects of this {@link OFXAggregate}.
     */
    public Iterable<OFXAggregateChild> getChildren()
    {
        return this.children;
    }

    @Override
    public Result<Integer> writeTo(CharacterWriteStream writeStream, OFXFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");

        return Result.create(() ->
        {
            int result = 0;

            result += writeStream.write('<').await();
            result += writeStream.write(this.getName()).await();
            result += writeStream.write('>').await();

            for (final OFXAggregateChild child : this.children)
            {
                result += child.writeTo(writeStream, format).await();
            }

            result += writeStream.write("</").await();
            result += writeStream.write(this.getName()).await();
            result += writeStream.write('>').await();

            return result;
        });
    }

    @Override
    public boolean equals(OFXAggregate rhs)
    {
        return rhs != null &&
            this.children.equals(rhs.children);
    }
}
