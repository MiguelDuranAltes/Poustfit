import HTTP from "@/communications_backend/common/http";

export default {

    async likePost(id) {
        return (await HTTP.post(`/posts/${id}/like`)).data;
    },

    async disLikePost(id) {
        return (await HTTP.post(`/posts/${id}/disLike`)).data;
    },

    async createPost(post) {
        return (await HTTP.post(`/post`, post)).data;
    },

    async addImagePost(id,imagen) {
        return (await HTTP.post(`/posts/${id}/imagen`,imagen)).data;
    },
};
