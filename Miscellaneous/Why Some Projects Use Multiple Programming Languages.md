Here are detailed notes for the YouTube video [Why Some Projects Use Multiple Programming Languages](https://www.youtube.com/watch?v=XJC5WB2Bwrc):

# Detailed Notes: Why Some Projects Use Multiple Programming Languages

This video, sponsored by Let's Get Rusty, aims to explain why some projects involve multiple programming languages in their development, focusing on low-level concepts.

## I. Types of Multi-Language Projects

The complexity of understanding multi-language projects depends on their type:

*   **Separate Processes:**
    *   **Example:** A full-stack framework like Django.
    *   **Languages:** Python for the backend (server-side) and HTML, CSS, JavaScript for the user interface (client-side).
    *   **Communication:** These are essentially two separate processes that communicate remotely at runtime using some form of interprocess communication (IPC).
    *   **Understanding:** This type is generally easier to understand as the processes are distinct.

*   **Single Process:**
    *   **Nature:** Components written in different programming languages are designed to run together as a single process.
    *   **Challenge:** This type is more complex, raising the question of how different languages can coexist within the same binary.

## II. Compilers and the Compilation Process

A common oversimplification is that compilers simply turn source code directly into executable files. In reality, it's a much more complex, multi-step process.

*   **Assumption:** For simplicity, the video initially considers only programming languages that compile down to machine code.
*   **Dedicated Compilers:** Generally, each programming language has its own dedicated compiler (e.g., a Rust file cannot be compiled with a Go compiler).
*   **The Core Question:** If languages have separate compilers, runtimes, and memory models, how can they live inside the same binary?

### A. The C Compilation Process (GCC Example)

Using a simple C program and GCC (GNU Compiler Collection) as an example, the compilation process can be broken down into four main phases:

1.  **Pre-processing:**
    *   **Purpose:** Prepares the source code for the next step.
    *   **Actions:**
        *   Removes comments.
        *   Expands macros.
        *   Resolves conditional compilation directives.
        *   **Crucially, resolves includes:** `include` directives are replaced with the contents of the header files (and any headers they include), effectively inserting that code into the source file before compilation begins.
    *   **Output:** Still C code, but pre-processed.

2.  **Compilation:**
    *   **Purpose:** Translates the pre-processed code.
    *   **Output:** **Assembly language**, which consists of human-readable instructions the computer will execute.
    *   **Myth Busted:** A compiler doesn't always convert source code directly into machine code; many convert it into an intermediate representation like assembly or even another programming language.

3.  **Assembling:**
    *   **Tool:** An assembler (technically another compiler).
    *   **Purpose:** Takes the human-readable assembly code from the previous phase and translates it into **machine code** (the ones and zeros your CPU understands).
    *   **Output:** An **object file**. This file is **not yet runnable**.

4.  **Linking:**
    *   **Purpose:** Combines multiple object files into a single, self-contained executable.
    *   **Sources of Object Files:** These can come from your own code or from external libraries included during development (e.g., the `printf` function from the C standard library).

    There are two main ways to link:

    *   **Static Linking:**
        *   **Method:** The machine code of each required function from a library is copied directly into the final executable.
        *   **Characteristics:** The output file is **self-contained** and ready to run.

    *   **Dynamic Linking:**
        *   **Method:** Instead of copying functions, the linker inserts a **reference** to the library that contains the machine instructions.
        *   **Libraries:** Libraries are pre-compiled into special files called **dynamic shared libraries** (e.g., `.SO` on Unix-like systems, `.DLL` on Windows). These libraries contain executable code for their functions but **lack an entry point** to start execution (like a `main` function).
        *   **Runtime Behavior:** At runtime, if the program needs a function from a dynamic library, the operating system loads the required function into the program's address space on demand.
        *   **Advantages:**
            *   **Efficiency:** Saves disk space and memory because only one copy of the library is stored on the system, and programs reference it, loading it only when needed. This is particularly beneficial for common functions like `printf` used by many programs.
            *   **Flexibility:** Libraries can be updated or patched without recompiling every program that uses them.

### B. Modularization and Exposing Compiler Phases

*   **Hiding Complexity:** Compilers like GCC are configured by default to hide these intermediate steps, showing only the final executable.
*   **Exposing Phases:** With the right flags, these phases can be exposed:
    *   `gcc -save-temps`: Generates the final executable plus all intermediate files.
    *   `gcc -S`: Stops the process after generating assembly code, useful for educational purposes or inspecting performance-critical code.
*   **Starting from Any Phase:** GCC can start compilation from any phase. For example, you can pass an assembly file to GCC and tell it to just assemble and link it. This modularity is key to multi-language projects.

## III. Multi-Language Projects in a Single Process: The Role of the Linker

The ability to mix components written in different programming languages in a single executable fundamentally comes down to the **linker**.

### A. Mixing Assembly and C

*   **Technique:** A common technique is to write most of the logic in a high-level language like C and implement performance-critical functions directly in assembly.
*   **Process:**
    1.  Write the heavy calculation function in assembly.
    2.  Call this assembly function from the C code.
    3.  Pass both the C file and the assembly file to GCC.
    4.  GCC will compile/assemble the C code and assemble the assembly code.
    5.  The linker will combine both object files into a single executable.
*   **Real-world Examples:** This technique is used in systems like the Linux kernel, FFmpeg, OpenSSL, and many embedded projects.

### B. GCC as a Toolchain / Compiler Collection

*   **Misconception:** What is casually called the "C compiler" (like GCC) is not just one compiler; it's a **toolchain** – a pipeline of tools executed in sequence.
*   **Pluggable Stages:** Each stage consumes the output of the previous one, and each tool is pluggable. You can replace parts of the toolchain or feed in your own files at various points.
*   **Support for Multiple Languages:** This modularity explains why GCC supports many programming languages beyond C, including C++, Objective-C, Fortran, Ada, D, and even Go (depending on configuration).
*   **Name Change:** Originally "GNU C Compiler," the acronym GCC was redefined to **"GNU Compiler Collection"** due to its expansion to support many languages. This understanding is crucial to avoid the misconception of GCC as a single "black box" for C code.

### C. Mixing High-Level Languages (e.g., Fortran and C)

*   **Possibility:** It's totally possible and common to mix high-level languages like Fortran and C.
*   **Process:** This typically involves multiple steps:
    1.  Compile and assemble the Fortran file (using its own pipeline, compiler, and potentially runtime dependencies).
    2.  Compile and assemble the C file.
    3.  The linker then combines both object files into a single executable.
*   **Linker's Role:** The key is the linker; the different languages don't even need to come from the same compiler suite.

### D. Mixing Rust and C

*   **Distinct Toolchains:** Rust has a completely different toolchain from C (different compiler, build system, philosophy).
*   **Shared Reliance:** However, when producing the final binary, Rust also relies on a linker.
*   **Calling Rust from C:**
    1.  Implement the function in Rust.
    2.  Compile the Rust code into a static or dynamic library.
    3.  Declare and use the function in the C code.
    4.  Compile the C code and link it with the Rust-compiled library.
*   **Calling C from Rust:** This also works the other way around and is actually more common.
    *   **Reason:** C is older, and many mature libraries and system APIs (e.g., graphics, cryptography, OS APIs) are written in C. Rust developers often need to "hook into" this existing ecosystem.

## IV. Reasons for Mixing Multiple Programming Languages

Developers choose to mix languages in projects for various reasons:

*   **Separation of Concerns/Processes:** As seen with full-stack frameworks like Django, different languages are used for distinct parts that communicate remotely (e.g., backend vs. frontend).
*   **Performance Optimization:**
    *   The entire system doesn't always need to be extremely fast, just certain parts.
    *   Developers often write most of the project in a high-level language for convenience and development speed.
    *   Performance-critical components are then implemented in a lower-level language like C or assembly.
*   **Leveraging Existing Ecosystems:** To integrate with mature libraries and system APIs often written in older, established languages like C (e.g., for graphics, cryptography, or operating system interactions).

## V. Challenges: Application Binary Interface (ABI)

Just because two languages produce object files and both have a linking phase doesn't mean they can automatically be linked correctly into one executable.

*   **Problem: Mismatching Assumptions:**
    *   **Calling Conventions:** Compilers for different languages might make different assumptions about how data (function parameters, return values) is passed between functions (e.g., which registers are used).
    *   **Data Interpretation:** Languages might also differ in how they interpret data (e.g., "pass by reference" vs. "pass by value" – one expects memory addresses, the other expects actual values in registers).
*   **Consequences of Mismatch:**
    *   Undefined behavior at runtime.
    *   Incorrect results.
    *   Program crashes.
    *   The linked binary becomes inconsistent.

*   **Solution: Application Binary Interface (ABI):**
    *   **Definition:** An ABI defines **how different components of binary code interact with each other through the hardware**.
    *   **Contrast with API:** Just as an API defines functions at the application level, an ABI defines low-level rules for binary interaction.
    *   **Conformity is Key:** When mixing two different languages, at least one of them (specifically, the part that interacts with the other language) **must conform to the other's ABI expectations**. This might involve modifying one language's code to dereference values or load values directly into registers to match the other's convention.

*   **Language Support for ABI Conformity:**
    *   Modern languages provide tools, keywords, and compiler flags to make this process easier, explicitly telling the compiler that a function will interact with code from another language and that the generated assembly should follow a specific ABI.
    *   **C:** `extern` keyword for external functions.
    *   **Rust:** `extern` keyword and `no_mangle` attribute.
    *   **Fortran:** `bind` attribute.
    *   **Go:** Special block of comments above `import "C"` to include C header files and even write inline C code.

The video concludes by stating that these low-level details are typically only considered when working at the system level and teases a future video on mixing compiled and interpreted languages.