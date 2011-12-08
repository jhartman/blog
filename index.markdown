---
layout: name
title: Home

section: Home
---

Welcome
=======

Josh Hartman here. I'm currently a Senior Software Engineer at LinkedIn, and a
graduate of the University of Florida. This is my soapbox for all topics software
related.

+--	{.section}
Blog
=====
Posts:
{% for post in site.posts limit:10 %}
<ul class="compact recent">
<li>
	<a href="{{ post.url }}" title="{{ post.excerpt }}">{{ post.title }}</a>
	<span class="date">{{ post.date | date_to_string }}</span> 
</li>
</ul>
{% endfor %}
=--

+-- {.section}

[LinkedIn](http://www.linkedin.com/in/joshuahartman)
====================================================

=--