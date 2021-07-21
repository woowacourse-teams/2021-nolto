import { useState } from 'react';

export type SnackBarType = 'error' | 'success' | null;

interface SnackBar {
  key: number;
  type: SnackBarType;
  text: string;
  setTimeoutId?: NodeJS.Timeout;
}

export type AddSnackBar = (type: SnackBarType, text: string) => void;

interface Props {
  maxSnackBarCount: number;
  snackBarRemainTime: number;
}

const useSnackBarProvider = ({
  maxSnackBarCount,
  snackBarRemainTime,
}: Props): { snackBars: SnackBar[]; addSnackBar: AddSnackBar } => {
  const [snackBars, setSnackBars] = useState<SnackBar[]>(
    [...Array(maxSnackBarCount)].map((_, index) => ({
      key: index,
      type: null,
      text: '',
    })),
  );

  const removeSnackBar = (key: number) => {
    setSnackBars((state) =>
      state.map((snackBar) => {
        if (snackBar.key !== key) {
          return snackBar;
        }

        return { ...snackBar, type: null };
      }),
    );
  };

  const addSnackBar: AddSnackBar = (type, text) => {
    if (!text) {
      console.error('빈 메세지는 스낵바에 등록할 수 없습니다.');

      return;
    }

    setSnackBars((state) => {
      const [firstSnackBar, ...restSnackBar] = state;

      clearTimeout(firstSnackBar?.setTimeoutId);

      const setTimeoutId = setTimeout(() => removeSnackBar(newSnackBar.key), snackBarRemainTime);

      const newSnackBar: SnackBar = {
        ...firstSnackBar,
        type,
        text,
        setTimeoutId,
      };

      return [...restSnackBar, newSnackBar];
    });
  };

  return {
    snackBars,
    addSnackBar,
  };
};

export default useSnackBarProvider;
