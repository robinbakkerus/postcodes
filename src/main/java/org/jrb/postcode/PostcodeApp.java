package org.jrb.postcode;

import static spark.Spark.get;

/**
 * Hello world!
 */
public final class PostcodeApp {
    private PostcodeApp() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
    	PostcodeRepo repo = new PostcodeRepo();
    	get("/postcode/get/:postcode", (req, res) -> repo.getLocation(req.params(":postcode")));
    }
    
    
}
