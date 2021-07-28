import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Feed, FilterType } from 'types';
import HttpError from 'utils/HttpError';
import ERROR_CODE from 'constants/errorCode';

interface SearchParams {
  query: string;
  techs: string;
  filter: FilterType;
}

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  searchParams: SearchParams;
  errorHandler?: ErrorHandler;
}

const getSearchResult = async (searchParams: SearchParams, errorHandler: ErrorHandler) => {
  const { query, techs, filter } = searchParams;
  const queryString = new URLSearchParams({ query, techs, filter: filter || '' });

  try {
    const { data } = await api.get('/feeds/search?' + queryString);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.errorMessage);

    throw new HttpError(
      status,
      ERROR_CODE[data.errorCode] || '검색 과정에서 에러가 발생했습니다',
      errorHandler,
    );
  }
};

const useSearch = ({ searchParams, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Feed[]>(
    ['searchResult', searchParams],
    () => getSearchResult(searchParams, errorHandler),
    option,
  );
};

export default useSearch;
