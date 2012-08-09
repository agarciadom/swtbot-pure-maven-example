#!/bin/bash

#
# SWTBot dependency installation script
# Copyright (C) 2011 Antonio García-Domínguez
#

set -e

install_module() {
    case "$#" in
	1)
	    mvn install:install-file "-DpomFile=$1" "-Dfile=$1"
	    ;;
	2)
	    mvn install:install-file "-DpomFile=$1" "-Dfile=$2"
	    ;;
	3)
	    mvn install:install-file "-DpomFile=$1" "-Dfile=$2" "-Dsources=$3"
	    ;;
	*)
	    echo "Usage: install_module pom [jar] [src.jar]" >&2
	    return 1
	    ;;
    esac
}

install_module swt/es.uca.swt-3.6.1.pom
install_module org.eclipse.core.commands-3.6.0.I20100512{.pom,.jar,-sources.jar}
install_module org.eclipse.equinox.common-3.6.0{.pom,.jar,-sources.jar}
install_module org.eclipse.jface-3.6.2.M20110210{.pom,.jar,-sources.jar}
install_module org.eclipse.osgi-3.6.2.R36x{.pom,.jar,-sources.jar}
install_module org.eclipse.swtbot.junit4_x-2.0.5{.pom,.jar}
install_module org.eclipse.swtbot.swt.finder-2.0.5{.pom,.jar}
