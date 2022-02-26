package qub;

/**
 * A base class of {@link OFXObject} implementations.
 */
public abstract class OFXObjectBase<T extends OFXObject> implements OFXObject
{
    @Override
    public String toString()
    {
        return OFXObject.toString(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return Types.instanceOf(rhs, this.getClass()) &&
            this.equals((T)rhs);
    }

    public abstract boolean equals(T rhs);
}
