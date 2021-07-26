import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
  formData: FormData;
}

const uploadFeed = ({ feedId, formData }: Args) => api.put(`/feeds/${feedId}`, formData);

export default function useFeedModify() {
  return useMutation<AxiosResponse<any>, HttpError, Args>(uploadFeed);
}
