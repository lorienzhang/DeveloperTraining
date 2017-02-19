package com.lorienzhang.stackoverflowxmlparser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * XML Feed的内容，解析出其中所有的Entry子标签；
 * 提取其中的title，link，summary
 *
 <feed xmlns="http://www.w3.org/2005/Atom" xmlns:creativeCommons="http://backend.userland.com/creativeCommonsRssModule" ...">
     <title type="text">newest questions tagged android - Stack Overflow</title>
     ...
     <entry>
     ...
     </entry>
     <entry>
         <id>http://stackoverflow.com/q/9439999</id>
         <re:rank scheme="http://stackoverflow.com">0</re:rank>
         <title type="text">Where is my data file?</title>
         <category scheme="http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest/tags" term="android"/>
         <category scheme="http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest/tags" term="file"/>
         <author>
         <name>cliff2310</name>
         <uri>http://stackoverflow.com/users/1128925</uri>
         </author>
         <link rel="alternate" href="http://stackoverflow.com/questions/9439999/where-is-my-data-file" />
         <published>2012-02-25T00:30:54Z</published>
         <updated>2012-02-25T00:30:54Z</updated>
         <summary type="html">
         <p>I have an Application that requires a data file...</p>

         </summary>
     </entry>
     <entry>
     ...
     </entry>
    ...
 </feed>
 */


/**
 * 从StackOverflow中down下一个XML Feed---InputStream
 * 利用Pull解析这个XML Feed，
 * 从中获取出所有的entry子标签；
 */
public class StackOverflowXmlParser {

    public static final String ns = null;

    public List parse(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);

        //START_DOCUMENT
        parser.next();
        List entries = readFeed(parser);

        return entries;
    }

    public static class Entry{
        public String title;
        public String link;
        public String summary;

        public Entry(String title, String link, String summary) {
            this.title = title;
            this.link = link;
            this.summary = summary;
        }
    }

    //解析Feed标签的entry子标签，如果不是entry子标签就skip
    private List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {

        List<Entry> list = new ArrayList();

        //测试当前是否在feed开始标签
        parser.require(XmlPullParser.START_TAG, ns, "feed");

        Entry entry;
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if("entry".equals(parser.getName())) {
                list.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return list;
    }

    // 解析Entry标签中的"title"子标签，"link"子标签，"summary"子标签
    // 并将内容保存在java bean（Entry）中
    private Entry readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        Entry entry;
        String title = null;
        String link = null;
        String summary = null;

        parser.require(XmlPullParser.START_TAG, ns, "entry");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if("title".equals(parser.getName())) {
                title = readTitle(parser);
            } else if("link".equals(parser.getName())) {
                link = readLink(parser);
            } else if ("summary".equals(parser.getName())) {
                summary = readSummary(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "entry");
        entry = new Entry(title, link, summary);

        return entry;
    }

    //解析summary子标签内容
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        String summary = null;
        //检查是否是summary的开始标签
        parser.require(XmlPullParser.START_TAG, ns, "summary");

        summary = readText(parser);

        //检查是否是summary的结束标签
        parser.require(XmlPullParser.END_TAG, ns, "summary");

        return summary;
    }

    //解析link子标签的href属性值
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = null;
        //检查是否是link的开始标签
        parser.require(XmlPullParser.START_TAG, ns, "link");

        String rel = parser.getAttributeValue(null, "rel");
        if("alternate".equals(rel)) {
            link = parser.getAttributeValue(null, "href");
            //几乎和next()一样，只不过nextTag()内部做了安全性判断
            parser.nextTag();
        }

        //检查是否是link的结束标签
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    //解析title子标签内容
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        //检查是否是title的开始标签
        parser.require(XmlPullParser.START_TAG, ns, "title");
        title = readText(parser);
        //检查是否是titile的结束标签
        parser.require(XmlPullParser.END_TAG, ns, "title");

        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String text = "";
        if(parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        return text;
    }

    //跳过不感兴趣的TAG---depth
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        //安全性检查
        if(parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        //depth减到0就代表当前Tag结束
        int depth = 1;
        while(depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.START_TAG:
                    depth++;
                    break;

                case XmlPullParser.END_TAG:
                    depth--;
                    break;
            }
        }
    }

}
