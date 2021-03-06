package net.willemml.ktcmd

val manager = CommandManager<Message>()

/**
 * For testing only, simplifies the testing process of making sure everything is working visually
 */
fun main() {
    val testCommandZero = Command<Message>("testZero", "Test for commands with no arguments.", arrayListOf("0")) {
        println("$name: $description")
        println("Test zero success!")
    }
    val testCommandOne = Command<Message>("testOne", "Test for commands with optional arguments only.", arrayListOf("t")) {
        println("$name: $description")
        getAnyArgument<String>("str")?.let { println("string test: $it") }
        getAnyArgument<Boolean>("bool")?.let { println("boolean test: $it") }
        getAnyArgument<Int>("int")?.let { println("int test: $it") }
        getAnyArgument<Long>("long")?.let { println("long test: $it") }
        getAnyArgument<Float>("float")?.let { println("float test: $it") }
        getAnyArgument<Double>("double")?.let { println("double test: $it") }
        try {
            println("int as string: ${getAnyArgument<String>("int")?: "null"}")
        } catch (e: IllegalArgumentException) {
            println("Invalid getOptionalArgument type test success")
        }
        println("Test one success!")
    }.apply {
        string("str", false, "An optional string argument for testing", "s")
        boolean("bool", false, "An optional boolean argument for testing", "b")
        integer("int", false, "An optional integer argument for testing", "i")
        long("long", false, "An optional long argument for testing", "l")
        float("float", false, "An optional float argument for testing", "f")
        double("double", false, "An optional double argument for testing", "d")
        println("All arguments registered.")
    }
    val testCommandTwo = Command<Message>("testTwo", "Test for commands with required arguments, also tests argument types.") {
        println("$name: $description")
        println("string: ${getArgument<String>("str")}")
        println("boolean: ${getArgument<Boolean>("bool")}")
        println("int: ${getArgument<Int>("int")}")
        println("long: ${getArgument<Long>("long")}")
        println("float: ${getArgument<Float>("float")}")
        println("double: ${getArgument<Double>("double")}")
        try {
            println("float as int: ${getArgument<Float>("float")}")
        } catch (e: IllegalArgumentException) {
            println("Invalid getArgument type test success")
        }
        println("Test two success!")
    }.apply {
        string("str", true, "A required string argument for testing", "s")
        boolean("bool", true, "A required boolean argument for testing", "b")
        integer("int", true, "A required integer argument for testing", "i")
        long("long", true, "A required long argument for testing", "l")
        float("float", true, "A required float argument for testing", "f")
        double("double", true, "A required double argument for testing", "d")

        try {
            string("str", true, "s")
        } catch (e: IllegalArgumentException) {
            println("Duplicate argument error check complete.")
        }
        println("All arguments registered.")
    }

    val testCommandThree = Command<Message>("testThree", "Test for commands with required and optional arguments, default arguments used and used for help test.") {
        println("default val for string: ${getOptionalArgument<String>("optstr")}")
        println("default val for boolean: ${getOptionalArgument<Boolean>("optbool")}")
        println("default val for int: ${getOptionalArgument<Int>("optint")}")
        println("default val for long: ${getOptionalArgument<Long>("optlong")}")
        println("default val for float: ${getOptionalArgument<Float>("optfloat")}")
        println("default val for double: ${getOptionalArgument<Double>("optdouble")}")
    }.apply {
        string("optstr", false, "An optional string argument for testing", "os", "This is the defualt value of this argument.")
        boolean("optbool", false, "An optional boolean argument for testing", "ob", true)
        integer("optint", false, "An optional integer argument for testing", "oi", 111)
        long("optlong", false, "An optional long argument for testing", "ol", 4211)
        float("optfloat", false, "An optional float argument for testing", "of", 11.02F)
        double("optdouble", false, "An optional double argument for testing", "od", 0.0112)
        string("reqstr", true, "A required string argument for testing", "rs")
        boolean("reqbool", true, "A required boolean argument for testing", "rb")
        integer("reqint", true, "A required integer argument for testing", "ri")
        long("reqlong", true, "A required long argument for testing", "rl")
        float("reqfloat", true, "A required float argument for testing", "rf")
        double("reqdouble", true, "A required double argument for testing", "rd")
    }
    val testCommandFour = Command<Message>("testFour", "Test for commands with required arguments, also tests argument types.", arrayListOf(), true) {
        println("$name: $description")
        println("string: ${getArgument<String>("str")}")
        println("boolean: ${getArgument<Boolean>("bool")}")
        println("int: ${getArgument<Int>("int")}")
        println("long: ${getArgument<Long>("long")}")
        println("float: ${getArgument<Float>("float")}")
        println("double: ${getArgument<Double>("double")}")
        try {
            println("float as int: ${getArgument<Float>("float")}")
        } catch (e: IllegalArgumentException) {
            println("Invalid getArgument type test success")
        }
        println("Test four success!")
    }.apply {
        string("str", true, "A string argument for testing")
        boolean("bool", true, "A boolean argument for testing")
        integer("int", true, "A integer argument for testing")
        long("long", true, "A long argument for testing")
        float("float", true, "A float argument for testing")
        double("double", true, "A double argument for testing")

        try {
            string("str", true, "s")
        } catch (e: IllegalArgumentException) {
            println("Duplicate argument error check complete.")
        }
        println("All arguments registered.")
    }

    manager.loadCommands(arrayOf(testCommandZero, testCommandOne, testCommandTwo, testCommandThree, testCommandFour))
    println("\n====== Testing argument parsing ======\nAliases:\n> 0")
    manager.runCommand(Message("0"))
    println("\n> t")
    manager.runCommand(Message("t"))
    println("\nArguments, optional:\nnone > testZero")
    manager.runCommand(Message("testZero"))
    println("\nnone > testOne")
    manager.runCommand(Message("testOne"))
    println("\nstring with spaces > testOne")
    manager.runCommand(Message("testOne --str \"Test, with spaces!\""))
    println("\nsome > testOne")
    manager.runCommand(Message("testOne --str test -i 42"))
    println("\nall > testOne --str test -i 42 --bool true -l=64 --float=3.2 --double 57.33")
    manager.runCommand(Message("testOne --str test -i 42 --bool true -l=64 --float=3.2 --double 57.33"))
    println("\nArguments, required:\nnone > testTwo")
    manager.runCommand(Message("testTwo"))
    println("\nsome > testTwo -s test")
    manager.runCommand(Message("testOne --str test -i 42"))
    println("\nall > testTwo --str test -b true -i 42 -l=64 --float=3.2 --double 57.33")
    manager.runCommand(Message("testTwo --str test -b true -i 42 -l=64 --float=3.2 --double 57.33"))
    println("\nWrong type\n> testOne -i test")
    manager.runCommand(Message("testOne -i test"))
    println("\nDefault\n> testThree -rs t -rb false -ri 0 -rl 0 -rf 0.0 -rd 0.0")
    manager.runCommand(Message("testThree -rs t -rb false -ri 0 -rl 0 -rf 0.0 -rd 0.0"))
    println("\nOrdered parsing:\nall > testFour test true 42 64 3.2 57.33")
    manager.runCommand(Message("testFour test true 42 64 3.2 57.33"))
    println("\nsome > testFour test true 42 64")
    manager.runCommand(Message("testFour test true 42 64"))
    println("\nnone > testFour")
    manager.runCommand(Message("testFour"))
    println("\n\n====== Testing help command ======\nWithout arguments:\n> help")
    manager.runCommand(Message("help"))
    println("\nARGUMENT FORMATS:\n> help --command testZero")
    manager.runCommand(Message("help --command testZero"))
    println("\n> help --command=testOne")
    manager.runCommand(Message("help --command=testOne"))
    println("\n> help -c testTwo")
    manager.runCommand(Message("help -c testTwo"))
    println("\n> help -c=testThree")
    manager.runCommand(Message("help -c testThree"))
    println("\n> help -c testFour")
    manager.runCommand(Message("help -c=testFour"))
    println("\nWith non existent command as argument:\n> help --command invalid")
    manager.runCommand(Message("help --command invalid"))
}

class Message(message: String) : Call(message) {
    override fun respond(message: String) {
        println(message)
    }
}