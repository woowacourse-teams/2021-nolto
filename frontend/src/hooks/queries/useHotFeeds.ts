import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { Feed } from 'types';
import HttpError from 'utils/HttpError';

type ErrorType = 'feeds-001' | 'feeds-002';

const getHotFeeds = async () => {
  try {
    const { data } = await api.get('/feeds/hot');

    return data;
  } catch (error) {
    console.error(error);

    const { status, data } = error.response;
    const errorMap: Record<ErrorType, string> = {
      ['feeds-001']: '임시 에러 메시지 1',
      ['feeds-002']: '임시 에러 메시지 2',
    };

    throw new HttpError(status, errorMap[data.error as ErrorType]);
  }
};

export default function useHotFeeds(option: UseQueryOptions<Feed[], HttpError>) {
  return useQuery<Feed[], HttpError>('hotFeeds', getHotFeeds, option);
}
