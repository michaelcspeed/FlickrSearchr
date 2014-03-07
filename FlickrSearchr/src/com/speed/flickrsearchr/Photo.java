package com.speed.flickrsearchr;

public class Photo {

	public Photo(int icLauncher, String string) {
		this.photoRes = icLauncher;
		this.title = string;
	}

	public Photo(String title, String farmid, String serverid, String id,
			String secret, int photoRes) {
		super();
		this.title = title;
		this.farmid = farmid;
		this.serverid = serverid;
		this.id = id;
		this.secret = secret;
		this.photoRes = photoRes;
	}

	String title;
	String farmid;
	String serverid;
	String id;
	String secret;
	int photoRes;

}
