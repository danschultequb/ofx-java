package qub;

/**
 * An object that is part of an OFX data stream.
 */
public interface OFXObject
{
    /**
     * Get the {@link String} representation of the provided {@link OFXObject}.
     * @param ofx The {@link OFXObject} to get the {@link String} representation of.
     */
    static String toString(OFXObject ofx)
    {
        PreCondition.assertNotNull(ofx, "ofx");

        return ofx.toString(OFXFormat.consise);
    }

    /**
     * Get the {@link String} representation of this {@link OFXObject} using the provided
     * {@link OFXFormat}.
     * @param format The {@link OFXFormat} to use when producing the {@link String} represention of
     *               this {@link OFXObject}.
     */
    default String toString(OFXFormat format)
    {
        PreCondition.assertNotNull(format, "format");

        final InMemoryCharacterStream stream = InMemoryCharacterStream.create();
        this.writeTo(stream, format).await();
        final String result = stream.getText().await();

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Write the {@link String} representation of this {@link OFXObject} to the provided
     * {@link CharacterWriteStream}.
     * @param writeStream The {@link CharacterWriteStream} to write to.
     * @return The number of characters that were written.
     */
    default Result<Integer> writeTo(CharacterWriteStream writeStream)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");

        return this.writeTo(writeStream, OFXFormat.consise);
    }

    /**
     * Write the {@link String} representation of this {@link OFXObject} to the provided
     * {@link CharacterWriteStream} using the provided {@link OFXFormat}.
     * @param writeStream The {@link CharacterWriteStream} to write to.
     * @param format The {@link OFXFormat} to use when writing this {@link OFXObject}.
     * @return The number of characters that were written.
     */
    Result<Integer> writeTo(CharacterWriteStream writeStream, OFXFormat format);
}
