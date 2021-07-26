import { useMutation } from 'react-query';

import api from 'constants/api';

const uploadFeed = (formData: FormData) => api.post('/feeds', formData);

export default function useFeedUpload() {
  return useMutation(uploadFeed);
}
