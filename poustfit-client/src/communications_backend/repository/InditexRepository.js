import HTTP from '@/communications_backend/common/http'

export default {
    async getInditexRecommendations(imageUrl, page, size) {
        const response = await HTTP.get(`/inditex/recommendations`, {
            params: { imageUrl, page, size },
        })
        return response.data
    },

    async getProductPhoto(name, url) {
        const response = await HTTP.post(`/playwright`, null, {
            params: { name, url },
            responseType: 'blob', // Indicar que la respuesta es un archivo binario
        })

        const blob = new Blob([response.data], { type: response.headers['content-type'] })
        return URL.createObjectURL(blob)
    },
}
