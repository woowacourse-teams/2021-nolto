import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
}

const deleteLike = ({ feedId }: Args) => api.delete(`/feeds/${feedId}/like`);

const useFeedUnlike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(deleteLike, option);
};

export default useFeedUnlike;
