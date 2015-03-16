# ODB-UI on Google Web Toolkit (codename) #
## What is this? ##
We have launched our vision for semantic interface a for public at "OpenSource Conference Ukraine - 2007" (October 27, 2007, [our short blog post](http://www.pleso.net/en/publications/tags/odb-ui/) about). And we have made a new prototype during spring 2008 and going to enter second round for a final product.

Currently it reassembles mind-map interface, but nodes would have own form-formatting rules and become more dynamic.

Prototype/alpha stage.

### [Demo video](http://www.pleso.net/en/publications/2008/10/15/user-web-interface-semantic-data-second-round/) ###

## Components ##
  * written on **Java**
  * **Google Web Toolkit** that generates all javascript (v1.4)
  * **RDF database** (Jena v2.5.5)
  * **Dojo** to draw SVG/VML lines in all browsers (v1.0.2)

## Concept ##

> http://gwt.org.ua/odbui/manual/fluxb_layout.html

## Live demo ##

> http://gwt.org.ua/odbui/ui-prototype2/odbui.html

## Demo manual ##

Many of us have the need to present the information on the web visually. This mind-map ODB-UI component meets this demand by offering content presentation in the same way the human mind works by visually connecting concepts and organizing them on top of each other into a general knowledge base. it will be more natural and comfortable for users to organize and share/publish ideas with our tool, comparing to the common writing plain text content/post on the web. We enable users to discuss the content publicly, navigate and search for information that is interconnected, and aggregate various independent sources.


> ![http://gwt.org.ua/odbui/manual/img/help1.png](http://gwt.org.ua/odbui/manual/img/help1.png)

> _General overview_


Just after opening ODB-UI, you'll get an interface similar to the picture above, saved graph would be restored from server. Look around, you'll see:

  1. **Main menu**:
    * ![http://gwt.org.ua/odbui/manual/button/load-button.gif](http://gwt.org.ua/odbui/manual/button/load-button.gif) _Load_ - restore graph from server
    * ![http://gwt.org.ua/odbui/manual/button/save-button.gif](http://gwt.org.ua/odbui/manual/button/save-button.gif) _Save_ - actually saves the graph
    * ![http://gwt.org.ua/odbui/manual/button/reset-button.gif](http://gwt.org.ua/odbui/manual/button/reset-button.gif) _Reset_ - brings you back to the default graph, substituting the current one
    * ![http://gwt.org.ua/odbui/manual/button/rdf.gif](http://gwt.org.ua/odbui/manual/button/rdf.gif) _Dump RDF_ - open a new window with the RDF code of currently saved graph on the server
    * ![http://gwt.org.ua/odbui/manual/button/start-autoalign-button.gif](http://gwt.org.ua/odbui/manual/button/start-autoalign-button.gif) _Start autoalign_ - would auto-allocate nodes for an optimal view. While Autoalign is working, the manual control would be ignored, but you could stop it by pressing on Stop autoalign - the same button.
    * ![http://gwt.org.ua/odbui/manual/button/addnode-button.gif](http://gwt.org.ua/odbui/manual/button/addnode-button.gif) _Add node_ - would create a new node without connections
  1. **Active node** - a node currently under a mouse pointer, it shows a toolbar with the functional buttons:
    * ![http://gwt.org.ua/odbui/manual/img/node_edit.gif](http://gwt.org.ua/odbui/manual/img/node_edit.gif) edit node button
    * ![http://gwt.org.ua/odbui/manual/img/node_delete.gif](http://gwt.org.ua/odbui/manual/img/node_delete.gif) delete node
    * ![http://gwt.org.ua/odbui/manual/img/node_create.gif](http://gwt.org.ua/odbui/manual/img/node_create.gif) create tool - to add new nodes or connections (usage example below)
    * ![http://gwt.org.ua/odbui/manual/img/node_fold.gif](http://gwt.org.ua/odbui/manual/img/node_fold.gif) fold/unfold node
    * **Drag'n'drop** - you could move a node around by holding it by the caption field
  1. **Folded node** - you could fold/unfold node pressing button in the caption field


> ![http://gwt.org.ua/odbui/manual/img/help2.gif](http://gwt.org.ua/odbui/manual/img/help2.gif)

> _How to create new nodes and connections_

Drag-n-drop ![http://gwt.org.ua/odbui/manual/img/node_create.gif](http://gwt.org.ua/odbui/manual/img/node_create.gif) icon to create new connections or nodes. If you'll release mouse button on spare place - a new node will be created there, if you'll release on existing node - would be created a new connection between nodes


> ![http://gwt.org.ua/odbui/manual/img/help3.png](http://gwt.org.ua/odbui/manual/img/help3.png)

> _More mouse actions_

  1. **Move whole graph together (pan)** - press left mouse button on spare place and drag
  1. **Delete connection or change its color** - connection toolbar would appear, if you'll press on wrench icon closer to a center of line. There are delete and color buttons
  1. **Change size** - by holding node's right bottom corner


> ![http://gwt.org.ua/odbui/manual/img/help4.png](http://gwt.org.ua/odbui/manual/img/help4.png)

> _Edit mode_

If you'll press ![http://gwt.org.ua/odbui/manual/img/node_edit.gif](http://gwt.org.ua/odbui/manual/img/node_edit.gif) edit button or double-click on caption field, node would be switched to edit mode. Edit button would be substituted by save and cancel changes buttons.
Press on **click to open** sign under title to unfold hidden rich-editor panel.


> ![http://gwt.org.ua/odbui/manual/img/help5.png](http://gwt.org.ua/odbui/manual/img/help5.png)

> _Side Bar_


This component works in single user mode. It have been tested on Firefox 2+ (Windows, Linux), Internet Explorer 6, Safari 3 (Windows). No performance optimizations have been conducted.

First prototype you could find on our site - http://gwt.org.ua/en/odb-ui-proto/