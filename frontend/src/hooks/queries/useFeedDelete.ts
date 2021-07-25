import { useMutation, UseMutationOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { AxiosResponse } from 'axios';

interface Args {
  feedId: number;
}

const deleteFeed = ({ feedId }: Args) => api.delete(`/feeds/${feedId}`);

export default function useFeedDelete(
  option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>,
) {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(deleteFeed, option);
}
