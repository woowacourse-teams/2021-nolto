interface SVGProps {
  width: string;
}

declare module '*.svg' {
  const content: ({ width }: SVGProps) => JSX.Element;

  export default content;
}
