Example Maven module running SWTBot
===================================

I wrote this short example to see how I could unit test an SWT application built with Maven, using the [SWTBot](http://swtbot.com/) framework.

The framework is distributed as a set of Eclipse plugins, so the natural choices are:

* Using Eclipse to run the tests manually. We can't accept this, as we want to run the tests automatically for continuous integration.

* Using PDE/Ant to run them automatically from a Eclipse instance. Our understanding is that PDE/Ant is rather unwieldy to set up and run, requiring a full-blown Eclipse installation.

* Using Maven Tycho. This would have been fine, except for the fact that our actual SWT app depends on a lot of pure Maven modules. Automating the generation of the associated OSGi bundles would require too much work.

Our ideal option was to run SWTBot in a pure Maven project, so we repackaged SWTBot and its dependencies (as listed in the OSGi *MANIFEST.MF* files) as regular Maven artifacts.

This repository is divided into three folders:

* **eclipse-plugin-projects** contains the Eclipse projects that we originally wrote and tested inside Eclipse. *SayHello* is the application itself, and *SayHello.tests* is a fragment project which tests the application. Our `IsolatedShellTest` superclass can be reused for any test suite which needs to recreate the main Shell of the application in every test.

* **maven-deps** contains a repackaged version of SWTBot 2.0.4 and its dependencies (based on Eclipse Helios 3.6.2), as pure Maven artifacts. If you're not using a repository manager, you can use `install.sh` to install those dependencies into your local repository.

* **maven-module/sayhello** is the mavenized version of the two previous Eclipse projects, using the repackaged SWTBot from above. You should change the <repository> inside `pom.xml` to the address of your repository manager, if you have one. If you have installed the dependencies through `maven-deps/install.sh`, you can also simply delete the <repositories> element.

Testing the SWT application is as simple as running the following command from the **maven-module/sayhello** directory:

    mvn test

The source code for these examples is under the new BSD license. See LICENSE for details.

