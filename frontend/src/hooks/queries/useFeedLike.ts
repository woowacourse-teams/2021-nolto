import { useMutation, UseMutationOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { AxiosResponse } from 'axios';

interface Args {
  feedId: number;
}

const postLike = ({ feedId }: Args) => api.post(`/feeds/${feedId}/like`);

const useFeedLike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(postLike, option);
};

export default useFeedLike;
