package simple.mind.jpathread;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import simple.mind.jpathread.model.Comments;

public class Parse {

	private static Date getDate(String date) throws ParseException {
		return Date.from(Instant.parse(date + "Z"));
	}


	public static Comments parseString(String l) throws ParseException {
		Map<String, String> ret = new HashMap<String, String>();
		l = l.replaceAll("^<row ", "").replaceAll("/>", "").trim();
		StringBuilder name = new StringBuilder();
		StringBuilder value = new StringBuilder();
		boolean isName = true;
		boolean isValue = false;
		for (int i = 0; i < l.length(); i++) {
			char c = l.charAt(i);
			if (isName) {
				if (c == '=') {
					isName = false;
				} else {
					name.append(c);
				}
			} else if (!isName && !isValue) {
				if (c == '"') {
					isValue = true;
				}
			} else if (!isName && isValue) {
				if (c == '"') {
					ret.put(name.toString().trim(), value.toString().trim());
					name = new StringBuilder();
					value = new StringBuilder();
					isName = true;
					isValue = false;
				} else {
					value.append(c);
				}
			}
		}
		Comments c = new Comments();
		c.setId(getInteger(ret.get("Id")));
		c.setPostId(getInteger(ret.get("PostId")));
		c.setScore(getInteger(ret.get("Score")));
		c.setText(ret.get("Text"));
		c.setCreationDate(getDate(ret.get("CreationDate")));
		c.setUserDisplayName(ret.containsKey("UserDisplayName") ? ret.get("UserDisplayName") : "");
		c.setUserId(getLong(ret.get("UserId")));
		c.setContentLicense(ret.get("ContentLicense"));
		return c;
	}

	private static Integer getInteger(String s) {
		if (s == null)
			return null;
		return Integer.valueOf(s);
	}

	private static Long getLong(String s) {
		if (s == null)
			return null;
		return Long.valueOf(s);
	}
}
