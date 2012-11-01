@XmlJavaTypeAdapters({@XmlJavaTypeAdapter(value = DateTimeAdapter.class, type = DateTime.class)})
package com.nedap.healthcare.kadasterbagclient.api.model;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.joda.time.DateTime;

import com.nedap.healthcare.xml.adapters.DateTimeAdapter;

