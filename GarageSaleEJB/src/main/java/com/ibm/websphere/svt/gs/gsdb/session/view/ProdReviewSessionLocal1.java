package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.ProdReview;

public interface ProdReviewSessionLocal1 {

	String createProdReview(ProdReview prodReview) throws Exception;

	String deleteProdReview(ProdReview prodReview) throws Exception;

	String updateProdReview(ProdReview prodReview) throws Exception;

	ProdReview findProdReviewByReviewId(long reviewId);

	ProdReview getNewProdReview();

}
