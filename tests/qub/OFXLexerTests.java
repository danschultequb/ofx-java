package qub;

public interface OFXLexerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OFXLexer.class, () ->
        {
            runner.testGroup("create(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXLexer.create((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });

                final Action2<String,Iterable<OFXLex>> createTest = (String text, Iterable<OFXLex> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXLexer lexer = OFXLexer.create(Strings.iterate(text));
                        test.assertNotNull(lexer);
                        test.assertFalse(lexer.hasStarted());
                        test.assertFalse(lexer.hasCurrent());

                        for (final OFXLex expectedLex : expected)
                        {
                            test.assertTrue(lexer.next());
                            test.assertTrue(lexer.hasStarted());
                            test.assertTrue(lexer.hasCurrent());
                            test.assertEqual(expectedLex, lexer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(lexer.next());
                            test.assertTrue(lexer.hasStarted());
                            test.assertFalse(lexer.hasCurrent());
                            test.assertThrows(lexer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run(
                    "<",
                    Iterable.create(
                        OFXLex.leftAngleBracket()));
                createTest.run(
                    ">",
                    Iterable.create(
                        OFXLex.rightAngleBracket()));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXLex.whitespace(" ")));
                createTest.run(
                    "\t",
                    Iterable.create(
                        OFXLex.whitespace("\t")));
                createTest.run(
                    "\r",
                    Iterable.create(
                        OFXLex.whitespace("\r")));
                createTest.run(
                    "\n",
                    Iterable.create(
                        OFXLex.whitespace("\n")));
                createTest.run(
                    "a",
                    Iterable.create(
                        OFXLex.text("a")));
                createTest.run(
                    ".",
                    Iterable.create(
                        OFXLex.text(".")));
                createTest.run(
                    "*",
                    Iterable.create(
                        OFXLex.text("*")));
                createTest.run(
                    "a<b> c < > d",
                    Iterable.create(
                        OFXLex.text("a"),
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("b"),
                        OFXLex.rightAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.text("c"),
                        OFXLex.whitespace(" "),
                        OFXLex.leftAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.rightAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.text("d")));
                createTest.run(
                    Strings.join('\n', Iterable.create(
                        "OFXHEADER:100",
                        "DATA:OFXSGML",
                        "<OFX>")),
                    Iterable.create(
                        OFXLex.text("OFXHEADER:100"),
                        OFXLex.whitespace("\n"),
                        OFXLex.text("DATA:OFXSGML"),
                        OFXLex.whitespace("\n"),
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("OFX"),
                        OFXLex.rightAngleBracket()));
            });

            runner.testGroup("create(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXLexer.create((CharacterReadStream)null),
                        new PreConditionFailure("readStream cannot be null."));
                });

                final Action2<String,Iterable<OFXLex>> createTest = (String text, Iterable<OFXLex> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXLexer lexer = OFXLexer.create(InMemoryCharacterStream.create(text).endOfStream());
                        test.assertNotNull(lexer);
                        test.assertFalse(lexer.hasStarted());
                        test.assertFalse(lexer.hasCurrent());

                        for (final OFXLex expectedLex : expected)
                        {
                            test.assertTrue(lexer.next());
                            test.assertTrue(lexer.hasStarted());
                            test.assertTrue(lexer.hasCurrent());
                            test.assertEqual(expectedLex, lexer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(lexer.next());
                            test.assertTrue(lexer.hasStarted());
                            test.assertFalse(lexer.hasCurrent());
                            test.assertThrows(lexer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run(
                    "<",
                    Iterable.create(
                        OFXLex.leftAngleBracket()));
                createTest.run(
                    ">",
                    Iterable.create(
                        OFXLex.rightAngleBracket()));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXLex.whitespace(" ")));
                createTest.run(
                    "\t",
                    Iterable.create(
                        OFXLex.whitespace("\t")));
                createTest.run(
                    "\r",
                    Iterable.create(
                        OFXLex.whitespace("\r")));
                createTest.run(
                    "\n",
                    Iterable.create(
                        OFXLex.whitespace("\n")));
                createTest.run(
                    "a",
                    Iterable.create(
                        OFXLex.text("a")));
                createTest.run(
                    ".",
                    Iterable.create(
                        OFXLex.text(".")));
                createTest.run(
                    "*",
                    Iterable.create(
                        OFXLex.text("*")));
                createTest.run(
                    "a<b> c < > d",
                    Iterable.create(
                        OFXLex.text("a"),
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("b"),
                        OFXLex.rightAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.text("c"),
                        OFXLex.whitespace(" "),
                        OFXLex.leftAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.rightAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.text("d")));
                createTest.run(
                    Strings.join('\n', Iterable.create(
                        "OFXHEADER:100",
                        "DATA:OFXSGML",
                        "<OFX>")),
                    Iterable.create(
                        OFXLex.text("OFXHEADER:100"),
                        OFXLex.whitespace("\n"),
                        OFXLex.text("DATA:OFXSGML"),
                        OFXLex.whitespace("\n"),
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("OFX"),
                        OFXLex.rightAngleBracket()));
            });

            runner.testGroup("create(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXLexer.create((String)null),
                        new PreConditionFailure("text cannot be null."));
                });

                final Action2<String,Iterable<OFXLex>> createTest = (String text, Iterable<OFXLex> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXLexer lexer = OFXLexer.create(text);
                        test.assertNotNull(lexer);
                        test.assertFalse(lexer.hasStarted());
                        test.assertFalse(lexer.hasCurrent());

                        for (final OFXLex expectedLex : expected)
                        {
                            test.assertTrue(lexer.next());
                            test.assertTrue(lexer.hasStarted());
                            test.assertTrue(lexer.hasCurrent());
                            test.assertEqual(expectedLex, lexer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(lexer.next());
                            test.assertTrue(lexer.hasStarted());
                            test.assertFalse(lexer.hasCurrent());
                            test.assertThrows(lexer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run(
                    "<",
                    Iterable.create(
                        OFXLex.leftAngleBracket()));
                createTest.run(
                    ">",
                    Iterable.create(
                        OFXLex.rightAngleBracket()));
                createTest.run(
                    "/",
                    Iterable.create(
                        OFXLex.forwardSlash()));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXLex.whitespace(" ")));
                createTest.run(
                    "\t",
                    Iterable.create(
                        OFXLex.whitespace("\t")));
                createTest.run(
                    "\r",
                    Iterable.create(
                        OFXLex.whitespace("\r")));
                createTest.run(
                    "\n",
                    Iterable.create(
                        OFXLex.whitespace("\n")));
                createTest.run(
                    "a",
                    Iterable.create(
                        OFXLex.text("a")));
                createTest.run(
                    ".",
                    Iterable.create(
                        OFXLex.text(".")));
                createTest.run(
                    "*",
                    Iterable.create(
                        OFXLex.text("*")));
                createTest.run(
                    "<a></a>",
                    Iterable.create(
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("a"),
                        OFXLex.rightAngleBracket(),
                        OFXLex.leftAngleBracket(),
                        OFXLex.forwardSlash(),
                        OFXLex.text("a"),
                        OFXLex.rightAngleBracket()));
                createTest.run(
                    "a<b> c < / > d e/",
                    Iterable.create(
                        OFXLex.text("a"),
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("b"),
                        OFXLex.rightAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.text("c"),
                        OFXLex.whitespace(" "),
                        OFXLex.leftAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.forwardSlash(),
                        OFXLex.whitespace(" "),
                        OFXLex.rightAngleBracket(),
                        OFXLex.whitespace(" "),
                        OFXLex.text("d"),
                        OFXLex.whitespace(" "),
                        OFXLex.text("e"),
                        OFXLex.forwardSlash()));
                createTest.run(
                    Strings.join('\n', Iterable.create(
                        "OFXHEADER:100",
                        "DATA:OFXSGML",
                        "<OFX>")),
                    Iterable.create(
                        OFXLex.text("OFXHEADER:100"),
                        OFXLex.whitespace("\n"),
                        OFXLex.text("DATA:OFXSGML"),
                        OFXLex.whitespace("\n"),
                        OFXLex.leftAngleBracket(),
                        OFXLex.text("OFX"),
                        OFXLex.rightAngleBracket()));
            });
        });
    }
}
