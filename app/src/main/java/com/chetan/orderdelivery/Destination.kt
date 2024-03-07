package com.chetan.orderdelivery

open class Destination(open val route: String) {

    object Screen{

        //common
        object CommonSignInScreen : Destination("common-sign-in-screen")


        //Admin
        object AdminDashboardScreen : Destination("admin-dashboard-screen")
        object AdminAddFoodScreen : Destination("admin-add-food-screen")
        object AdminRatingUpdateScreen : Destination("admin-rating-update-screen")
        object AdminOrderDetailScreen : Destination("admin-order-detail-screen/{user}")
        object AdminSendNoticeScreen : Destination("admin-send-notice-screen")
        object AdminEditFoodScreen : Destination("admin-edit-food-screen/{foodId}")
        object AdminAddOfferScreen : Destination("admin-add-offer-screen")
        object AdminNotificationScreen: Destination("admin-notification-screen")
        object AdminOrderHistory: Destination("admin-order-history")


        //User
        object UserDashboardScreen : Destination("user-dashboard-screen")
        object UserFoodOrderDescriptionScreen : Destination("user-food-order-description-screen/{foodId}")
        object UserOrderCheckoutScreen: Destination("user-order-checkout-screen/{totalCost}")
        object UserNotificationScreen: Destination("user-notification-screen")
        object UserMoreFoodScreen: Destination("user-more-food-screen")
        object UserMyOrderScreen: Destination("user-my-order-screen")
        object UserMoreScreen: Destination("user-more-screen")
        object UserSearchScreen: Destination("user-search-screen")
        object UserFoodCategoryScreen: Destination("user-food-category-screen")
        object UserProfileScreen: Destination("user-profile-screen/{isCompleteBack}")
    }
}