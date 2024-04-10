# T.H.O.M.A.S.

Repository for all modules of the T.H.O.M.A.S. project

## About T.H.O.M.A.S.
The acronym stands for "*Tool for Handling Operations, Management, and Assistance Strategies*".

And it's also my son's name.

The objective of this project is to be a P.O.C. (Proof of Concept), a clean architecture study, where any framework can be used for information I/O, and it is not necessary to rewrite any business rules, just the plugins of the frameworks that were used.

The context modules are separated, so that they can be used to generate a single service (monolith), or smaller services (microservices). Initially, they are all in the same repository, but with modularization, they are isolated within their respective contexts and can easily be separated.  

This project is for personal use.

## Contribution

Any and all PRs and comments are welcome, but please, explain the reason for the change, if possible, with examples.

## Repository

### Catalog:
Isolated project to manage all dependencies and versions for all modules

### Service:
All the project modules - dependency from Catalog Project

### Core:
The main module that all the other modules will use

### Infrastructure:
The holder of the modules that contains the base for developing

The submodules are divided according to the feature

### Module:
Holds modules tha implements business rules, the data and service modules cannot have any external dependency, of any lib or framework

The I/O for rest, database, messaging, etc., modules use the implementations of the base modules in infrastructure, as they are the plugins for their respective modules