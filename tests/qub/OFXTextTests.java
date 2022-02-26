package qub;

public interface OFXTextTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OFXText.class, () ->
        {
            runner.testGroup("whitespace(String)", () ->
            {
                final Action2<String,Throwable> whitespaceErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> OFXText.whitespace(text),
                            expected);
                    });
                };

                whitespaceErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                whitespaceErrorTest.run("", new PreConditionFailure("text cannot be empty."));

                final Action1<String> whitespaceTest = (String text) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXText token = OFXText.whitespace(text);
                        test.assertNotNull(token);
                        test.assertEqual(OFXTokenType.Whitespace, token.getType());
                        test.assertEqual(text, token.getText());
                    });
                };

                whitespaceTest.run(" ");
                whitespaceTest.run("abc");
            });

            runner.testGroup("text(String)", () ->
            {
                final Action2<String,Throwable> textErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> OFXText.text(text),
                            expected);
                    });
                };

                textErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                textErrorTest.run("", new PreConditionFailure("text cannot be empty."));

                final Action1<String> textTest = (String text) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXText token = OFXText.text(text);
                        test.assertNotNull(token);
                        test.assertEqual(OFXTokenType.Text, token.getType());
                        test.assertEqual(text, token.getText());
                    });
                };

                textTest.run("abc");
                textTest.run(" ");
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<OFXText,String> toStringTest = (OFXText text, String expected) ->
                {
                    runner.test("with " + text.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, text.toString());
                    });
                };

                toStringTest.run(OFXText.text("hello"), "hello");
                toStringTest.run(OFXText.text("abc"), "abc");
                toStringTest.run(OFXText.whitespace(" "), " ");
                toStringTest.run(OFXText.whitespace(" \n\t "), " \n\t ");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<OFXText,Object,Boolean> equalsTest = (OFXText text, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(text, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, text.equals(rhs));
                    });
                };

                equalsTest.run(OFXText.text("a"), null, false);
                equalsTest.run(OFXText.text("a"), "a", false);
                equalsTest.run(OFXText.text("a"), OFXText.text("a"), true);
                equalsTest.run(OFXText.text("a"), OFXText.text("b"), false);
                equalsTest.run(OFXText.text("a"), OFXText.whitespace("a"), false);
                equalsTest.run(OFXText.text("a"), OFXText.whitespace("b"), false);
                equalsTest.run(OFXText.text("a"), OFXTag.startTag("a"), false);
            });

            runner.testGroup("equals(OFXText)", () ->
            {
                final Action3<OFXText,OFXText,Boolean> equalsTest = (OFXText text, OFXText rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(text, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, text.equals(rhs));
                    });
                };

                equalsTest.run(OFXText.text("a"), null, false);
                equalsTest.run(OFXText.text("a"), OFXText.text("a"), true);
                equalsTest.run(OFXText.text("a"), OFXText.text("b"), false);
                equalsTest.run(OFXText.text("a"), OFXText.whitespace("a"), false);
                equalsTest.run(OFXText.text("a"), OFXText.whitespace("b"), false);
            });
        });
    }
}
