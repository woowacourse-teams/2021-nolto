import { useMutation } from 'react-query';

import api from 'constants/api';

const postFeed = (formData: FormData) => api.post('/feeds', formData);

export default function useUploadFeed() {
  return useMutation(postFeed);
}
