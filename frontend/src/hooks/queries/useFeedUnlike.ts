import { useMutation, UseMutationOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { AxiosResponse } from 'axios';

interface Args {
  feedId: number;
}

const deleteLike = ({ feedId }: Args) => api.delete(`/feeds/${feedId}/like`);

const useFeedUnlike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(deleteLike, option);
};

export default useFeedUnlike;
