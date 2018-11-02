
This project aims at providing a framework to perform behavioural testing of SPLs.

![VIBeS Logo](logo-vibes.png)

[![Build Status](https://travis-ci.org/xdevroey/vibes.svg?branch=master)](https://travis-ci.org/xdevroey/vibes)
[![Coverage Status](https://coveralls.io/repos/github/xdevroey/vibes/badge.svg?branch=master)](https://coveralls.io/github/xdevroey/vibes?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/be.unamur.info/vibes/badge.svg)](https://maven-badges.herokuapp.com/maven-central/be.unamur.info/vibes/badge.svg)

# Project structure

Structure separates the framework modules from the different executables jars created using those modules.

* vibes: The root project with the website content
	* vibes-core: The models definition core lib
	* vibes-execution, vibes-mutation, ... : The different modules defined to perform various testing activities
	* vibes-dsl: The dsl definition to simply use the different modules (each library has its dsl classes defined in a separate package)
	* vibes-toolbox: The module containing all executable jars built using the framework. Each executable jar is defined as a sub-module. Executables MUST be defined as vibes-toolbox SUB-MODULES.

