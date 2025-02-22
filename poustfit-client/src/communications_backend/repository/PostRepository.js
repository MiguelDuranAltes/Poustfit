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

    async saveImage(id, file) {
        const formData = new FormData();
        formData.append("file", file);
        const response = await HTTP.post(`/posts/${id}/imagen`, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    },

    async getPost(id) {
        return (await HTTP.get(`/posts/${id}`)).data;
    }
};
