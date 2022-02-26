package qub;

public interface OFXLexTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OFXLex.class, () ->
        {
            runner.test("leftAngleBracket()", (Test test) ->
            {
                final OFXLex leftAngleBracket = OFXLex.leftAngleBracket();
                test.assertNotNull(leftAngleBracket);
                test.assertEqual(OFXLexType.LeftAngleBracket, leftAngleBracket.getType());
                test.assertEqual("<", leftAngleBracket.getText());
                test.assertSame(leftAngleBracket, OFXLex.leftAngleBracket());
            });

            runner.test("rightAngleBracket()", (Test test) ->
            {
                final OFXLex rightAngleBracket = OFXLex.rightAngleBracket();
                test.assertNotNull(rightAngleBracket);
                test.assertEqual(OFXLexType.RightAngleBracket, rightAngleBracket.getType());
                test.assertEqual(">", rightAngleBracket.getText());
                test.assertSame(rightAngleBracket, OFXLex.rightAngleBracket());
            });

            runner.test("forwardSlash()", (Test test) ->
            {
                final OFXLex forwardSlash = OFXLex.forwardSlash();
                test.assertNotNull(forwardSlash);
                test.assertEqual(OFXLexType.ForwardSlash, forwardSlash.getType());
                test.assertEqual("/", forwardSlash.getText());
                test.assertSame(forwardSlash, OFXLex.forwardSlash());
            });

            runner.testGroup("whitespace(String)", () ->
            {
                final Action2<String,Throwable> whitespaceErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> OFXLex.whitespace(text),
                            expected);
                    });
                };

                whitespaceErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                whitespaceErrorTest.run("", new PreConditionFailure("text cannot be empty."));

                final Action1<String> whitespaceTest = (String text) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXLex lex1 = OFXLex.whitespace(text);
                        test.assertNotNull(lex1);
                        test.assertEqual(OFXLexType.Whitespace, lex1.getType());
                        test.assertEqual(text, lex1.getText());

                        final OFXLex lex2 = OFXLex.whitespace(text);
                        test.assertNotNull(lex2);
                        test.assertEqual(lex1, lex2);
                        test.assertNotSame(lex1, lex2);
                    });
                };

                whitespaceTest.run(" ");
                whitespaceTest.run("\t");
                whitespaceTest.run("     ");
                whitespaceTest.run("abc");
            });



            runner.testGroup("text(String)", () ->
            {
                final Action2<String,Throwable> textErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> OFXLex.text(text),
                            expected);
                    });
                };

                textErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                textErrorTest.run("", new PreConditionFailure("text cannot be empty."));

                final Action1<String> textTest = (String text) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXLex lex1 = OFXLex.text(text);
                        test.assertNotNull(lex1);
                        test.assertEqual(OFXLexType.Text, lex1.getType());
                        test.assertEqual(text, lex1.getText());

                        final OFXLex lex2 = OFXLex.text(text);
                        test.assertNotNull(lex2);
                        test.assertEqual(lex1, lex2);
                        test.assertNotSame(lex1, lex2);
                    });
                };

                textTest.run(" ");
                textTest.run("\t");
                textTest.run("     ");
                textTest.run("abc");
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<OFXLex,String> toStringTest = (OFXLex lex, String expected) ->
                {
                    runner.test("with " + lex.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, lex.toString());
                    });
                };

                toStringTest.run(OFXLex.leftAngleBracket(), "Type: LeftAngleBracket, Text: \"<\"");
                toStringTest.run(OFXLex.rightAngleBracket(), "Type: RightAngleBracket, Text: \">\"");
                toStringTest.run(OFXLex.forwardSlash(), "Type: ForwardSlash, Text: \"/\"");
                toStringTest.run(OFXLex.whitespace("\r\n"), "Type: Whitespace, Text: \"\\r\\n\"");
                toStringTest.run(OFXLex.text("Hello!"), "Type: Text, Text: \"Hello!\"");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<OFXLex,Object,Boolean> equalsTest = (OFXLex lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(OFXLex.leftAngleBracket(), null, false);
                equalsTest.run(OFXLex.leftAngleBracket(), "<", false);
                equalsTest.run(OFXLex.leftAngleBracket(), OFXLex.leftAngleBracket(), true);
                equalsTest.run(OFXLex.leftAngleBracket(), OFXLex.rightAngleBracket(), false);
                equalsTest.run(OFXLex.leftAngleBracket(), OFXLex.text("<"), false);
                equalsTest.run(OFXLex.text("abc"), OFXLex.text("def"), false);
            });

            runner.testGroup("equals(OFXLex)", () ->
            {
                final Action3<OFXLex,OFXLex,Boolean> equalsTest = (OFXLex lhs, OFXLex rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(OFXLex.leftAngleBracket(), null, false);
                equalsTest.run(OFXLex.leftAngleBracket(), OFXLex.leftAngleBracket(), true);
                equalsTest.run(OFXLex.leftAngleBracket(), OFXLex.rightAngleBracket(), false);
                equalsTest.run(OFXLex.leftAngleBracket(), OFXLex.text("<"), false);
                equalsTest.run(OFXLex.text("abc"), OFXLex.text("def"), false);
            });
        });
    }
}
