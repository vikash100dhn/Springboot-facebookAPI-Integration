package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.AgeRange;
import org.springframework.social.facebook.api.Experience;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PaymentPricePoints;
import org.springframework.social.facebook.api.PlaceTag;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.SecuritySettings;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.FacebookDetailsDTO;

@RestController
@RequestMapping("/home")
public class HomeController {

	@RequestMapping("/welcome")
	public String home() {
		return "Welcome Boot with fasf!";
	}
	@RequestMapping(value="/facebook/{accessToken}")
	public ResponseEntity<FacebookDetailsDTO> facebookDetails(@PathVariable String accessToken) {
		System.out.println("The value of accessToken is:"+accessToken);

		Facebook facebook = new FacebookTemplate(accessToken);
		UserOperations userOperations = facebook.userOperations();
		String[] fieldsToMap = {"id", "about", "age_range", "birthday",
				"context", "cover", "currency", "devices", "education",
				"email", "favorite_athletes", "favorite_teams",
				"first_name", "gender", "hometown", "inspirational_people",
				"installed", "install_type", "is_verified", "languages",
				"last_name", "link", "locale", "location", "meeting_for",
				"middle_name", "name", "name_format", "political",
				"quotes", "payment_pricepoints", "relationship_status",
				"religion", "security_settings", "significant_other",
				"sports", "test_group", "timezone", "third_party_id",
				"updated_time", "verified", "viewer_can_send_gift",
				"website", "work"};
		User userProfile = facebook.fetchObject("me", User.class, fieldsToMap);

		String id = userProfile.getId();
		System.out.println("Id:"+id);
		String about =userProfile.getAbout();
		System.out.println("About:"+about);
		AgeRange ageRange = userProfile.getAgeRange();
		System.out.println("Age range is:"+ageRange.toString());
		List<Reference> favoriteAtheletes = userProfile.getFavoriteAtheletes();
		for(Reference r : favoriteAtheletes)
		{
			System.out.println("Atheletes:"+r.getName());
		}
		List<Reference> favoriteTeams = userProfile.getFavoriteTeams();

		if(null != favoriteTeams) {
			for(Reference r : favoriteTeams)
			{
				System.out.println("Teams:"+r.getName());
			}
		}


		List<Reference> inspirationalPeople = userProfile.getInspirationalPeople();

		if(null != inspirationalPeople) {

			for(Reference r : inspirationalPeople)
			{
				System.out.println("(Inspirational People:"+r.getName());
			}
		}

		Locale locale = userProfile.getLocale();
		System.out.println("Country:"+locale.getCountry());
		Reference location = userProfile.getLocation();
		System.out.println("Location:"+location.getName());
		String political = userProfile.getPolitical();
		System.out.println("Political:"+political);
		String quotes = userProfile.getQuotes();
		System.out.println("Quotes"+quotes);

		String relationShipStatus = userProfile.getRelationshipStatus();
		System.out.println("Status:"+relationShipStatus);
		String religion = userProfile.getReligion();
		System.out.println("Religion:"+religion);

		SecuritySettings securitySettings = userProfile.getSecuritySettings();
		if(null != securitySettings)
		{
			System.out.println("SecuritySettings:"+securitySettings.getSecureBrowsing().isEnabled());
		}

		Reference significantOther = userProfile.getSignificantOther();
		if(null != significantOther)
		{
			System.out.println("Significant Others:"+significantOther);
		}
		List<Experience> sports = userProfile.getSports();
		if(null !=sports)
		{
			for(Experience r : sports)
			{
				System.out.println("Sports:"+r.getName());
			}
		}
		int testGroup = userProfile.getTestGroup();

		List<String> friendsIds = facebook.friendOperations().getFriendIds();
		for(String frnid : friendsIds){
			org.springframework.social.facebook.api.User user = facebook.userOperations().getUserProfile(frnid);

			System.out.println(user.getEmail());
			System.out.println(user.getFirstName());
			System.out.println(user.getLastName());
		}

		PagedList<Post> feed = facebook.feedOperations().getFeed();
		if (null != feed && !feed.isEmpty()) {
			boolean flag = true;
			int postCount = 0;
			List<String> kbaCheckInPlace = new ArrayList<String>();
			for (Post post : feed) {
				if (null != post.getPlace() && null != post.getPlace() && flag && null != post.getPlace().getLocation()
						&& null != post.getPlace().getLocation().getCity()) {

					flag = false;
					kbaCheckInPlace.add(post.getPlace().getLocation().getCity());
					System.out.println("Checked in place:"+post.getPlace().getLocation().getCity());;

				}
				Date createdTime = post.getCreatedTime();
				/*if (compareDateForOneYear(createdTime)) {
					postCount++;
				}*/
			}

			System.out.println("Post Count:"+String.valueOf(postCount));
		}

		List<PlaceTag> taggedPlaces = userOperations.getTaggedPlaces();
		for(PlaceTag places :taggedPlaces) {
			System.out.println("tagged places:"+places.getPlace().getName());
		}
		//Getting the feed/ status update of the user
		//facebook.feedOperations().updateStatus("Hi Everyone!");
		List<Post> feeds = facebook.feedOperations().getFeed();
		for(Post post : feeds)
		{
			System.out.println(post.getMessage());
		}
		//Getting the likes of the user
		//	List<Page> interests = facebook
		PagedList<Page> pageLiked = facebook.likeOperations().getPagesLiked();
		//System.out.println("size of page Liked:"+pageLiked.getTotalCount());

		for(int i=0; i<pageLiked.getTotalCount(); i++)
		{
			System.out.println("Page Name:"+pageLiked.get(i).getName()+"Page Category:"+pageLiked.get(i).getCategory());
		}
		//page.getCategoryList().toArray()

		FacebookDetailsDTO facebookDetailsDTO = new FacebookDetailsDTO();
		ResponseEntity<FacebookDetailsDTO> response = new  ResponseEntity<>(HttpStatus.OK);
		return response ;

	}

}
