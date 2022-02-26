package qub;

public abstract class OFXTokenBase<T extends OFXToken> extends OFXObjectBase<T> implements OFXToken
{
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return Types.instanceOf(rhs, this.getClass()) &&
            this.equals((T)rhs);
    }

    public abstract boolean equals(T rhs);
}
