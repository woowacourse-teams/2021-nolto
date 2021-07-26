import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
}

const deleteFeed = ({ feedId }: Args) => api.delete(`/feeds/${feedId}`);

export default function useFeedDelete(
  option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>,
) {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(deleteFeed, option);
}
