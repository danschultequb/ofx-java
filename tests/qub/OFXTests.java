package qub;

public interface OFXTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OFX.class, () ->
        {
            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> OFX.parse(text).await(),
                            expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("", new ParseException("Missing root."));
                parseErrorTest.run("   ", new ParseException("Missing root."));
                parseErrorTest.run("a b c", new ParseException("Missing root."));
                parseErrorTest.run("</a>", new ParseException("Expected text or root start tag, but found </a> instead."));
                parseErrorTest.run("<a>", new ParseException("Missing aggregate end tag or element data for <a>."));
                parseErrorTest.run("<a>  ", new ParseException("Missing element data or aggregate children for <a>."));
                parseErrorTest.run("<a></a>", new ParseException("Missing aggregate children."));
                parseErrorTest.run("<a><b>", new ParseException("Missing aggregate end tag or element data for <b>."));

                final Action2<String,OFXDocument> parseTest = (String text, OFXDocument expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, OFX.parse(text).await());
                    });
                };

                parseTest.run(
                    "<a>text",
                    OFXDocument.create(
                        "",
                        OFXElement.create("a", "text")));
                parseTest.run(
                    "<a> hello there ",
                    OFXDocument.create(
                        "",
                        OFXElement.create("a", " hello there ")));
                parseTest.run(
                    "fake:header<a><b><c>cats<d>dogs</b></a>",
                    OFXDocument.create(
                        "fake:header",
                        OFXAggregate.create("a", Iterable.create(
                            OFXAggregate.create("b", Iterable.create(
                                OFXElement.create("c", "cats"),
                                OFXElement.create("d", "dogs")))))));
            });
        });
    }
}
