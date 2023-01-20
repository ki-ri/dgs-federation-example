package com.example.demo.datafetchers;

import com.example.demo.generated.types.*;
import com.netflix.graphql.dgs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DgsComponent
public class ReviewsDatafetcher {

    Map<String, List<Review>> reviews = new HashMap<>();
    Map<String, CareManager> careManagerMap = new HashMap<>();

    public ReviewsDatafetcher() {
        List<Review> review1 = new ArrayList<>();
        review1.add(new Review(5, "hoge"));
        review1.add(new Review(4, "foo"));
        review1.add(new Review(5, "bar"));
        reviews.put("1", review1);

        List<Review> review2 = new ArrayList<>();
        review2.add(new Review(3, "fuga"));
        review2.add(new Review(5, "piyo"));
        reviews.put("2", review2);



        CareManager careManager1 = new CareManager("care manager", "1", new Office("1"), new Address("China", "Shanghai"));
        CareManager careManager2 = new CareManager("care manager", "2", new Office("2"), new Address("Japan", "Yokohama"));
        careManagerMap.put(careManager1.getOfficeId(), careManager1);
        careManagerMap.put(careManager2.getOfficeId(), careManager2);
    }

    @DgsQuery
    public CareManager careManager(@InputArgument String officeId) {
        return careManagerMap.get(officeId);
    }

    @DgsEntityFetcher(name = "Show")
    public Show movie(Map<String, Object> values) {
        return new Show((String) values.get("id"), null);
    }

    @DgsData(parentType = "Show", field = "reviews")
    public List<Review> reviewsFetcher(DgsDataFetchingEnvironment dataFetchingEnvironment)  {
        Show show = dataFetchingEnvironment.getSource();
        return reviews.get(show.getId());
    }
}
