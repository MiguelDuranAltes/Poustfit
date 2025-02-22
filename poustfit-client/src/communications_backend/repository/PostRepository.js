import HTTP from "@/communications_backend/common/http";

export default {

    async likePost(id) {
        return (await HTTP.post(`/posts/${id}/like`)).data;
    },

    async disLikePost(id) {
        return (await HTTP.post(`/posts/${id}/disLike`)).data;
    },

    async getAllPosts(page, size) {
        const response = await HTTP.get(`/posts`, { params: { page, size } });
        return response.data;
    },

    async createPost(post) {
        return (await HTTP.post(`/posts`, post)).data;
    },

    async addImagePost(id,imagen) {
        return (await HTTP.post(`/posts/${id}/imagen`,imagen)).data;
    },
};
