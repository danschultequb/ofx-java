package qub;

public interface OFXTagTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OFXTag.class, () ->
        {
            runner.testGroup("startTag(String)", () ->
            {
                final Action2<String,Throwable> startTagErrorTest = (String name, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(name), (Test test) ->
                    {
                        test.assertThrows(() -> OFXTag.startTag(name),
                            expected);
                    });
                };

                startTagErrorTest.run(null, new PreConditionFailure("name cannot be null."));
                startTagErrorTest.run("", new PreConditionFailure("name cannot be empty."));

                final Action1<String> startTagTest = (String name) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(name), (Test test) ->
                    {
                        final OFXTag tag = OFXTag.startTag(name);
                        test.assertNotNull(tag);
                        test.assertEqual(OFXTokenType.StartTag, tag.getType());
                        test.assertEqual(name, tag.getName());
                    });
                };

                startTagTest.run(" ");
                startTagTest.run("abc");
            });

            runner.testGroup("endTag(String)", () ->
            {
                final Action2<String,Throwable> endTagErrorTest = (String name, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(name), (Test test) ->
                    {
                        test.assertThrows(() -> OFXTag.endTag(name),
                            expected);
                    });
                };

                endTagErrorTest.run(null, new PreConditionFailure("name cannot be null."));
                endTagErrorTest.run("", new PreConditionFailure("name cannot be empty."));

                final Action1<String> endTagTest = (String name) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(name), (Test test) ->
                    {
                        final OFXTag tag = OFXTag.endTag(name);
                        test.assertNotNull(tag);
                        test.assertEqual(OFXTokenType.EndTag, tag.getType());
                        test.assertEqual(name, tag.getName());
                    });
                };

                endTagTest.run("abc");
                endTagTest.run(" ");
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<OFXTag,String> toStringTest = (OFXTag tag, String expected) ->
                {
                    runner.test("with " + tag.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, tag.toString());
                    });
                };

                toStringTest.run(OFXTag.startTag("hello"), "<hello>");
                toStringTest.run(OFXTag.startTag("abc"), "<abc>");
                toStringTest.run(OFXTag.endTag(" "), "</ >");
                toStringTest.run(OFXTag.endTag(" \n\t "), "</ \n\t >");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<OFXTag,Object,Boolean> equalsTest = (OFXTag tag, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(tag, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, tag.equals(rhs));
                    });
                };

                equalsTest.run(OFXTag.startTag("a"), null, false);
                equalsTest.run(OFXTag.startTag("a"), "a", false);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.startTag("a"), true);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.startTag("b"), false);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.endTag("a"), false);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.endTag("b"), false);
                equalsTest.run(OFXTag.startTag("a"), OFXText.text("a"), false);
            });

            runner.testGroup("equals(OFXTag)", () ->
            {
                final Action3<OFXTag,OFXTag,Boolean> equalsTest = (OFXTag tag, OFXTag rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(tag, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, tag.equals(rhs));
                    });
                };

                equalsTest.run(OFXTag.startTag("a"), null, false);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.startTag("a"), true);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.startTag("b"), false);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.endTag("a"), false);
                equalsTest.run(OFXTag.startTag("a"), OFXTag.endTag("b"), false);
            });
        });
    }
}
