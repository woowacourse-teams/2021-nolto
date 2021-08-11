import { useState } from 'react';
import { AddSnackbar, SnackbarType } from 'types';

interface Snackbar {
  key: number;
  type: SnackbarType;
  text: string;
  setTimeoutId?: NodeJS.Timeout;
}

interface Props {
  maxSnackbarCount: number;
  snackbarRemainTime: number;
}

const useSnackbarProvider = ({
  maxSnackbarCount,
  snackbarRemainTime,
}: Props): { snackbars: Snackbar[]; addSnackbar: AddSnackbar } => {
  const [snackbars, setSnackbars] = useState<Snackbar[]>(
    [...Array(maxSnackbarCount)].map((_, index) => ({
      key: index,
      type: null,
      text: '',
    })),
  );

  const removeSnackbar = (key: number) => {
    setSnackbars((state) =>
      state.map((snackbar) => {
        if (snackbar.key !== key) {
          return snackbar;
        }

        return { ...snackbar, type: null };
      }),
    );
  };

  const addSnackbar: AddSnackbar = (type, text) => {
    if (!text) {
      console.error('빈 메세지는 스낵바에 등록할 수 없습니다.');

      return;
    }

    setSnackbars((state) => {
      const [firstSnackbar, ...restSnackbar] = state;

      clearTimeout(firstSnackbar.setTimeoutId);

      const setTimeoutId = setTimeout(() => removeSnackbar(newSnackbar.key), snackbarRemainTime);

      const newSnackbar: Snackbar = {
        key: firstSnackbar.key,
        type,
        text,
        setTimeoutId,
      };

      return [...restSnackbar, newSnackbar];
    });
  };

  return {
    snackbars,
    addSnackbar,
  };
};

export default useSnackbarProvider;
