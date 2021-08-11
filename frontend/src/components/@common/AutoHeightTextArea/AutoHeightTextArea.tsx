import React, { FormEvent, useEffect, useRef } from 'react';

import Styled from './AutoHeightTextArea.styles';

interface Props {
  initialValue: string;
  onChange: (inputValue: string) => void;
  className?: string;
}

const AutoHeightTextArea = ({ initialValue, onChange, className }: Props) => {
  const ref = useRef<HTMLDivElement>();

  const handleChangeInput = (event: FormEvent<HTMLDivElement>) => {
    onChange(event.currentTarget.innerText);
  };

  useEffect(() => {
    if (!ref.current) return;

    ref.current.innerText = initialValue;
  }, []);

  return (
    <Styled.Root
      role="textbox"
      ref={ref}
      spellCheck={false}
      onInput={handleChangeInput}
      className={className}
      contentEditable
    ></Styled.Root>
  );
};

export default AutoHeightTextArea;
