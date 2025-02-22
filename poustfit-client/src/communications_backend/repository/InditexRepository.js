import HTTP from "@/communications_backend/common/http";

export default {

    async getInditexRecommendations(imageUrl, page, size) {
        const response = await HTTP.get(`/inditex/recommendations`, { params: { imageUrl, page, size } });
        return response.data;
    }
};
