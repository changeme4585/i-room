// API 기본 설정
const API_CONFIG = {
    gateway: "http://localhost:8080"
};

/**
 * HTTP 요청을 처리하는 기본 fetch 래퍼
 * @param {string} url - 요청 URL
 * @param {object} options - fetch 옵션
 * @returns {Promise} 응답 데이터
 */
const apiRequest = async (url, options = {}) => {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJhZG1pbkBpcm9vbS5jb20iLCJyb2xlIjoiUk9MRV9TVVBFUl9BRE1JTiIsImlhdCI6MTc1MzkyOTU5OCwiZXhwIjoxNzU0NzkzNTk4fQ.4JFItxcbWjOF2cD2Yb8R8QZctvG5_dZWbnEabYXWpFk',
            ...options.headers,
        },
        ...options,
    };

    try {
        console.log(`API 요청: ${options.method || 'GET'} ${url}`);

        const response = await fetch(url, defaultOptions);

        console.log(`API 응답: ${response.status} ${response.statusText}`);

        // 응답 처리
        if (!response.ok) {
            const errorText = await response.text();
            console.error('API 오류 응답:', errorText);

            throw new Error(
                response.status === 404
                    ? '요청한 리소스를 찾을 수 없습니다.'
                    : response.status === 500
                        ? '서버 내부 오류가 발생했습니다.'
                        : response.status === 400
                            ? errorText || '잘못된 요청입니다.'
                            : `HTTP ${response.status}: ${response.statusText}`
            );
        }

        // JSON 응답 파싱
        const data = await response.json();
        console.log('API 응답 데이터:', data);

        return data;
    } catch (error) {
        console.error('API 요청 실패:', error);

        // 네트워크 오류 처리
        if (error.name === 'TypeError' && error.message.includes('fetch')) {
            throw new Error('서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.');
        }
        throw error;
    }
};

/**
 * User API 서비스
 */
export const userAPI = {

    /**
     * 근로자 목록 조회
     * @param {object} options
     * @param {number} options.page - 페이지 번호 (기본값: 0)
     * @param {number} options.size - 페이지당 개수 (기본값: 10)
     * @param {string} [options.target] - 검색 대상 (name, email)
     * @param {string} [options.keyword] - 검색어
     */
    getWorkers: async ({ page = 0, size = 10, target = '', keyword = '' } = {}) => {
        const queryParams = new URLSearchParams({
            page: page.toString(),
            size: size.toString(),
        });

        // target과 keyword가 모두 있을 때만 검색 파라미터 추가
        if (target && keyword && keyword.trim()) {
            queryParams.append('target', target);
            queryParams.append('keyword', keyword.trim());
        }

        const url = `${API_CONFIG.gateway}/api/user/workers?${queryParams.toString()}`;
        console.log('[요청 URL]', url);
        return await apiRequest(url);
    },


    /**
     * 근로자 정보 수정
     * @param {string} workerId - 근로자 ID
     * @param {object} workerData - 수정할 근로자 데이터
     * @returns {Promise} 수정된 근로자 정보
     */
    updateWorker: async (workerId, workerData) => {
        const url = `${API_CONFIG.gateway}/api/user/workers/${workerId}`;
        return await apiRequest(url, {
            method: 'PUT',
            body: JSON.stringify(workerData)
        });
    },

};