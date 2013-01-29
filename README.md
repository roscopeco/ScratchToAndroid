Scratch2Android (Convert Scratch project files to Android apps)
===============================================================

This project aims to convert project files from [MIT's Scratch](http://scratch.mit.edu/) (v1.4) programming environment into [Android](http://www.android.com/) applications.

*Please note that this is currently in very early development, and is little more than a proof of concept at present. Only a subset of Scratch is supported.*

This software is Copyright &copy; Ross Bamford (& contributors) and is Open Source software licensed under the Apache License 2.0. See the included LICENSE file for more details.

Prerequisites
-------------

To build Android projects from your Scratch projects, the following are *required* dependencies:

	* [Apache Ant](http://ant.apache.org/) version 1.5 or higher (Recommended 1.6+).
	* [Android SDK](http://developer.android.com/sdk/index.html) for building your generated projects.

The following, although not _required_, are highly *recommended* as they will make building and deploying your projects much easier:

	* [Eclipse IDE](http://eclipse.org/) (Recommend _Eclipse Juno_)
	* [Android ADT for Eclipse](http://developer.android.com/tools/sdk/eclipse-adt.html)

Please note that, at present, ScratchToAndroid is not a "one-stop" solution for building Android applications from your Scratch projects. If you are not comfortable using the tools mentioned above, then you are quite likely to run into problems. Please do not request support for such issues from the Scratch2Android authors.

Usage
-----

Android projects are generated from the included template project using Apache Ant. If you are running Linux (or possibly Mac OS X) a bash wrapper script is included that will take care of running Ant for you, passing in the appropriate options. An example command:

`$ ./generate samples/simple3.sb -n Simple3 -p com.roscopeco.scratch.sample3`

An explanation of the options can be found by running generate with a single `-h` option.

If you are on Windows, you will have to run Ant manually, passing in the options as properties. The equivalent of the above command would be:

`c:\> ant -buildfile generate.xml -DSCRATCHFILE=samples\simple3.sb -DPROJECTNAME=Simple3 -DPKG=com.roscopeco.scratch.simple3`

Samples
-------

A small number of sample Scratch projects are included with the distribution, in the `samples` directory. These Scratch projects do not make use of any features that are not currently supported by the Android runtime.

Notes
-----

This project depends on the [ScratchFileReader](https://github.com/roscopeco/ScratchFileReader) and [ScratchToJava](https://github.com/roscopeco/ScratchFileReader) projects. The requisite Jars are included in the `gen-lib` directory for convenience. If you wish to update them, you will need to check out both projects and use their Ant build scripts to generate new Jars. In this case, you'll also need to place the updated scratchtojava-runtime-VERSION.jar in the `libs/` directory.

