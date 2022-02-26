package qub;

public interface OFXTokenizerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OFXTokenizer.class, () ->
        {
            runner.testGroup("create(OFXLexer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXTokenizer.create((OFXLexer)null),
                        new PreConditionFailure("lexer cannot be null."));
                });

                final Action2<String,Throwable> createErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(OFXLexer.create(text));
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        test.assertThrows(tokenizer::await, expected);
                    });
                };

                createErrorTest.run("<", new ParseException("Missing start tag name or end tag forward slash ('/')."));
                createErrorTest.run("< ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \" \" instead."));
                createErrorTest.run("<  ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \"  \" instead."));
                createErrorTest.run("<abc", new ParseException("Missing start tag right angle bracket ('>')."));
                createErrorTest.run("<abc ", new ParseException("Expected start tag right angle bracket ('>'), but found \" \" instead."));
                createErrorTest.run("</", new ParseException("Missing end tag name."));
                createErrorTest.run("</ ", new ParseException("Expected end tag name, but found \" \" instead."));
                createErrorTest.run("<//", new ParseException("Expected end tag name, but found \"/\" instead."));
                createErrorTest.run("</>", new ParseException("Expected end tag name, but found \">\" instead."));
                createErrorTest.run("</hello", new ParseException("Missing end tag right angle bracket ('>')."));
                createErrorTest.run("</hello ", new ParseException("Expected end tag right angle bracket ('>'), but found \" \" instead."));

                final Action2<String,Iterable<OFXToken>> createTest = (String text, Iterable<OFXToken> expectedTokens) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(OFXLexer.create(text));
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        for (final OFXToken expectedToken : expectedTokens)
                        {
                            test.assertTrue(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertTrue(tokenizer.hasCurrent());
                            test.assertEqual(expectedToken, tokenizer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertFalse(tokenizer.hasCurrent());
                            test.assertThrows(tokenizer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run(
                    "<a>",
                    Iterable.create(
                        OFXTag.startTag("a")));
                createTest.run(
                    "</hello>",
                    Iterable.create(
                        OFXTag.endTag("hello")));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXText.whitespace(" ")));
                createTest.run(
                    "   ",
                    Iterable.create(
                        OFXText.whitespace("   ")));
                createTest.run(
                    "there",
                    Iterable.create(
                        OFXText.text("there")));
            });

            runner.testGroup("create(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXTokenizer.create((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });

                final Action2<String,Throwable> createErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(Strings.iterate(text));
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        test.assertThrows(tokenizer::await, expected);
                    });
                };

                createErrorTest.run("<", new ParseException("Missing start tag name or end tag forward slash ('/')."));
                createErrorTest.run("< ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \" \" instead."));
                createErrorTest.run("<  ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \"  \" instead."));
                createErrorTest.run("<abc", new ParseException("Missing start tag right angle bracket ('>')."));
                createErrorTest.run("<abc ", new ParseException("Expected start tag right angle bracket ('>'), but found \" \" instead."));
                createErrorTest.run("</", new ParseException("Missing end tag name."));
                createErrorTest.run("</ ", new ParseException("Expected end tag name, but found \" \" instead."));
                createErrorTest.run("<//", new ParseException("Expected end tag name, but found \"/\" instead."));
                createErrorTest.run("</>", new ParseException("Expected end tag name, but found \">\" instead."));
                createErrorTest.run("</hello", new ParseException("Missing end tag right angle bracket ('>')."));
                createErrorTest.run("</hello ", new ParseException("Expected end tag right angle bracket ('>'), but found \" \" instead."));

                final Action2<String,Iterable<OFXToken>> createTest = (String text, Iterable<OFXToken> expectedTokens) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(OFXLexer.create(text));
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        for (final OFXToken expectedToken : expectedTokens)
                        {
                            test.assertTrue(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertTrue(tokenizer.hasCurrent());
                            test.assertEqual(expectedToken, tokenizer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertFalse(tokenizer.hasCurrent());
                            test.assertThrows(tokenizer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run(
                    "<a>",
                    Iterable.create(
                        OFXTag.startTag("a")));
                createTest.run(
                    "</hello>",
                    Iterable.create(
                        OFXTag.endTag("hello")));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXText.whitespace(" ")));
                createTest.run(
                    "   ",
                    Iterable.create(
                        OFXText.whitespace("   ")));
                createTest.run(
                    "there",
                    Iterable.create(
                        OFXText.text("there")));
            });

            runner.testGroup("create(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXTokenizer.create((CharacterReadStream)null),
                        new PreConditionFailure("readStream cannot be null."));
                });

                final Action2<String,Throwable> createErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(InMemoryCharacterStream.create(text).endOfStream());
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        test.assertThrows(tokenizer::await, expected);
                    });
                };

                createErrorTest.run("<", new ParseException("Missing start tag name or end tag forward slash ('/')."));
                createErrorTest.run("< ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \" \" instead."));
                createErrorTest.run("<  ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \"  \" instead."));
                createErrorTest.run("<abc", new ParseException("Missing start tag right angle bracket ('>')."));
                createErrorTest.run("<abc ", new ParseException("Expected start tag right angle bracket ('>'), but found \" \" instead."));
                createErrorTest.run("</", new ParseException("Missing end tag name."));
                createErrorTest.run("</ ", new ParseException("Expected end tag name, but found \" \" instead."));
                createErrorTest.run("<//", new ParseException("Expected end tag name, but found \"/\" instead."));
                createErrorTest.run("</>", new ParseException("Expected end tag name, but found \">\" instead."));
                createErrorTest.run("</hello", new ParseException("Missing end tag right angle bracket ('>')."));
                createErrorTest.run("</hello ", new ParseException("Expected end tag right angle bracket ('>'), but found \" \" instead."));

                final Action2<String,Iterable<OFXToken>> createTest = (String text, Iterable<OFXToken> expectedTokens) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(OFXLexer.create(text));
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        for (final OFXToken expectedToken : expectedTokens)
                        {
                            test.assertTrue(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertTrue(tokenizer.hasCurrent());
                            test.assertEqual(expectedToken, tokenizer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertFalse(tokenizer.hasCurrent());
                            test.assertThrows(tokenizer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run(
                    "<a>",
                    Iterable.create(
                        OFXTag.startTag("a")));
                createTest.run(
                    "</hello>",
                    Iterable.create(
                        OFXTag.endTag("hello")));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXText.whitespace(" ")));
                createTest.run(
                    "   ",
                    Iterable.create(
                        OFXText.whitespace("   ")));
                createTest.run(
                    "there",
                    Iterable.create(
                        OFXText.text("there")));
            });

            runner.testGroup("create(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> OFXTokenizer.create((String)null),
                        new PreConditionFailure("text cannot be null."));
                });

                final Action2<String,Throwable> createErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(text);
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        test.assertThrows(tokenizer::await, expected);
                    });
                };

                createErrorTest.run("<", new ParseException("Missing start tag name or end tag forward slash ('/')."));
                createErrorTest.run("< ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \" \" instead."));
                createErrorTest.run("<  ", new ParseException("Expected start tag name or end tag forward slash ('/'), but found \"  \" instead."));
                createErrorTest.run("<abc", new ParseException("Missing start tag right angle bracket ('>')."));
                createErrorTest.run("<abc ", new ParseException("Expected start tag right angle bracket ('>'), but found \" \" instead."));
                createErrorTest.run("</", new ParseException("Missing end tag name."));
                createErrorTest.run("</ ", new ParseException("Expected end tag name, but found \" \" instead."));
                createErrorTest.run("<//", new ParseException("Expected end tag name, but found \"/\" instead."));
                createErrorTest.run("</>", new ParseException("Expected end tag name, but found \">\" instead."));
                createErrorTest.run("</hello", new ParseException("Missing end tag right angle bracket ('>')."));
                createErrorTest.run("</hello ", new ParseException("Expected end tag right angle bracket ('>'), but found \" \" instead."));
                final Action2<String,Iterable<OFXToken>> createTest = (String text, Iterable<OFXToken> expectedTokens) ->
                {
                    runner.test("with non-null lexer with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final OFXTokenizer tokenizer = OFXTokenizer.create(OFXLexer.create(text));
                        test.assertNotNull(tokenizer);
                        test.assertFalse(tokenizer.hasStarted());
                        test.assertFalse(tokenizer.hasCurrent());

                        for (final OFXToken expectedToken : expectedTokens)
                        {
                            test.assertTrue(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertTrue(tokenizer.hasCurrent());
                            test.assertEqual(expectedToken, tokenizer.getCurrent());
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(tokenizer.next());
                            test.assertTrue(tokenizer.hasStarted());
                            test.assertFalse(tokenizer.hasCurrent());
                            test.assertThrows(tokenizer::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                createTest.run(
                    "",
                    Iterable.create());
                createTest.run("/",
                    Iterable.create(
                        OFXText.text("/")));
                createTest.run(">",
                    Iterable.create(
                        OFXText.text(">")));
                createTest.run(
                    "<a>",
                    Iterable.create(
                        OFXTag.startTag("a")));
                createTest.run(
                    "</hello>",
                    Iterable.create(
                        OFXTag.endTag("hello")));
                createTest.run(
                    " ",
                    Iterable.create(
                        OFXText.whitespace(" ")));
                createTest.run(
                    "   ",
                    Iterable.create(
                        OFXText.whitespace("   ")));
                createTest.run(
                    "there",
                    Iterable.create(
                        OFXText.text("there")));
                createTest.run(
                    "<a><b>hello<c>there</a>",
                    Iterable.create(
                        OFXTag.startTag("a"),
                        OFXTag.startTag("b"),
                        OFXText.text("hello"),
                        OFXTag.startTag("c"),
                        OFXText.text("there"),
                        OFXTag.endTag("a")));
            });
        });
    }
}
